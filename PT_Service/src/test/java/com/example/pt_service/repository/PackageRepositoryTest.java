package com.example.pt_service.repository;

import com.example.pt_service.repository.packaze.PackageEntity;
import com.example.pt_service.repository.packaze.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Test
    @DisplayName("save테스트")
    public void test_save(){
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @Test
    @DisplayName("FindByTest")
    public void test_findByCreatedAtAfter(){
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1); // 지금시점에서 1분전으로 설정

        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("학생 전용 3개월째");
        packageEntity0.setPeriod(90);
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월째");
        packageEntity1.setPeriod(180);
        packageRepository.save(packageEntity1);

        //when
        final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0,1, Sort.by("packageSeq").descending()));

        //then
        assertEquals(1,packageEntities.size());
        assertEquals(packageEntity1.getPackageSeq(),packageEntities.get(0).getPackageSeq());
    }

    @Test
    @DisplayName("UpdateTest")
    public void test_updateCountAndPeriod(){
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월째");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //when
        int updateCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(),30,120);
        final PackageEntity updatePackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertEquals(1,updateCount);
        assertEquals(30,updatePackageEntity.getCount());
        assertEquals(120,updatePackageEntity.getPeriod());
    }

    @Test
    @DisplayName("DeleteTest")
    public void test_delete(){
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(newPackageEntity.getPackageSeq());


        //then
        assertTrue(packageRepository.findById(newPackageEntity.getPackageSeq()).isEmpty());
    }
}
