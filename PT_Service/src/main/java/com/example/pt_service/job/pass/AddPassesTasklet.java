package com.example.pt_service.job.pass;

import com.example.pt_service.repository.pass.*;
import com.example.pt_service.repository.user.UserGroupMappingEntity;
import com.example.pt_service.repository.user.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

    private final PassRepository passRepository;

    private final BulkPassRepository bulkPassRepository;

    private final UserGroupMappingRepository userGroupMappingRepository;

    public AddPassesTasklet(PassRepository passRepository,BulkPassRepository bulkPassRepository,UserGroupMappingRepository userGroupMappingRepository){
        this.passRepository = passRepository;
        this.bulkPassRepository = bulkPassRepository;
        this.userGroupMappingRepository=userGroupMappingRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        //이용권 시작 1일전 user group 내 각 사용자에게 이용권 추가
        final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        final List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY,startedAt);

        int count = 0;

        //대량 이용권 정보를 돌면서 user group에 속한 user ID를 조회하고 헤당 userId로 이용권을 추가
        for(BulkPassEntity bulkPassEntity:bulkPassEntities){
            final List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
                    .stream().map(UserGroupMappingEntity::getUserId).collect(Collectors.toList());

            count += addPasses(bulkPassEntity,userIds);

            bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        }
        log.info("AddPassesTasklet - execute : 이용권 {}건 추가완료, startedAt={}",count,startedAt);
        return RepeatStatus.FINISHED;

    }

    //bulkPass의 정보로 pass 데이터 생성
    private int addPasses(BulkPassEntity bulkPassEntity, List<String> userIds){
        List<PassEntity> passEntities = new ArrayList<>();

        for(String userId: userIds){
            PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);
            passEntities.add(passEntity);
        }
        return passRepository.saveAll(passEntities).size();

    }
}
