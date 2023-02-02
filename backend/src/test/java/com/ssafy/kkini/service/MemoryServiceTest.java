package com.ssafy.kkini.service;

import com.ssafy.kkini.dto.MemoryCreateFormDto;
import com.ssafy.kkini.dto.MemoryUpdateFormDto;
import com.ssafy.kkini.dto.UserCreateFormDto;
import com.ssafy.kkini.entity.Memory;
import com.ssafy.kkini.entity.Photo;
import com.ssafy.kkini.entity.User;
import com.ssafy.kkini.repository.MemoryRepository;
import com.ssafy.kkini.repository.PhotoRpository;
import com.ssafy.kkini.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class MemoryServiceTest {
    @InjectMocks
    MemoryService memoryService;
    @Mock
    MemoryRepository memoryRepository;
    @Mock
    PhotoRpository photoRepository;
    @Mock
    UserRepository userRepository;

    @Value("{upload.path}")
    private String fileDir;

    @DisplayName("Memory create service TEST")
    @Test
    void createTest() throws IOException {
        //given
        String path1 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image1.jpg";
        String path2 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image2.png";
        MockMultipartFile uploadFile1 = new MockMultipartFile("file1", "Image1.jpg","image/jpg",new FileInputStream(new File(path1)));
        MockMultipartFile uploadFile2 = new MockMultipartFile("file2", "Image2.png","image/png",new FileInputStream(new File(path2)));

        List<MultipartFile> arrayList = new ArrayList<>();
        arrayList.add(uploadFile1);
        arrayList.add(uploadFile2);

        UserCreateFormDto userCreateFormDto = new UserCreateFormDto(
                "여민지"
                ,"minji@naver.com"
                ,"1234"
                ,"밍"
                ,"F"
                ,19980901);
        MemoryCreateFormDto memoryCreateFormDto = new MemoryCreateFormDto("오늘 식단", "성공적", 2L, arrayList);
        Memory memory = memoryCreateFormDto.toEntity();
        Optional<User> user = Optional.ofNullable(userCreateFormDto.toEntity());
        when(userRepository.findAllByUserId(any())).thenReturn(user);
        when(memoryRepository.save(any())).thenReturn(memory);

        //when
        Memory result = memoryService.createMemory(memoryCreateFormDto);

        //then
        Assertions.assertThat(memory.getMemoryTitle()).isEqualTo(result.getMemoryTitle());

    }

    @DisplayName("photo create service TEST")
    @Test
    void createPhoto() throws IOException {
        //given
        String path1 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image1.jpg";
        String path2 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image2.png";
        MockMultipartFile uploadFile1 = new MockMultipartFile("file1", "Image1.jpg","image/jpg",new FileInputStream(new File(path1)));
        MockMultipartFile uploadFile2 = new MockMultipartFile("file2", "Image2.png","image/png",new FileInputStream(new File(path2)));

        List<MultipartFile> arrayList = new ArrayList<>();
        arrayList.add(uploadFile1);
        arrayList.add(uploadFile2);

        MemoryCreateFormDto memoryCreateFormDto = new MemoryCreateFormDto("오늘 식단", "성공적", 2L, arrayList);
        Memory memory = memoryCreateFormDto.toEntity();
        List<Photo> photolist = makePhotoEntity(arrayList,memory);

        for (Photo photo : photolist) {
            when(photoRepository.save(any())).thenReturn(photo);
        }

        //when
        ArrayList<Photo> result = memoryService.uploadPhoto(arrayList,memory);

        //then
        Assertions.assertThat(result.size()).isEqualTo(photolist.size());
    }
    @DisplayName("memory update service TEST")
    @Test
    void updateMemory() throws IOException{
        //given
        String path1 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image1.jpg";
        String path2 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image2.png";
        MockMultipartFile uploadFile1 = new MockMultipartFile("file1", "Image1.jpg","image/jpg",new FileInputStream(new File(path1)));
        MockMultipartFile uploadFile2 = new MockMultipartFile("file2", "Image2.png","image/png",new FileInputStream(new File(path2)));

        List<MultipartFile> arrayList = new ArrayList<>();
        arrayList.add(uploadFile1);
        arrayList.add(uploadFile2);

        UserCreateFormDto userCreateFormDto = new UserCreateFormDto(
                "여민지"
                ,"minji@naver.com"
                ,"1234"
                ,"밍"
                ,"F"
                ,19980901);
        MemoryUpdateFormDto memoryUpdateFormDto = new MemoryUpdateFormDto(1L,"오늘 식단", "성공적", 1L, arrayList);
        Memory memory = memoryUpdateFormDto.toEntity();
        Optional<User> user = Optional.ofNullable(userCreateFormDto.toEntity());
//        when(userRepository.findAllByUserId(any())).thenReturn(user);
        when(memoryRepository.findById(any())).thenReturn(Optional.ofNullable(memory));
        when(memoryRepository.save(any())).thenReturn(memory);

        //when
        Memory result = memoryService.updateMemory(memoryUpdateFormDto);

        //then
        Assertions.assertThat(memory.getMemoryTitle()).isEqualTo(result.getMemoryTitle());
    }

    @DisplayName("photo delete service TEST")
    @Test
    void deletePhoto() throws IOException{
        //given
        String path1 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image1.jpg";
        String path2 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image2.png";
        MockMultipartFile uploadFile1 = new MockMultipartFile("file1", "Image1.jpg","image/jpg",new FileInputStream(new File(path1)));
        MockMultipartFile uploadFile2 = new MockMultipartFile("file2", "Image2.png","image/png",new FileInputStream(new File(path2)));

        List<MultipartFile> arrayList = new ArrayList<>();
        arrayList.add(uploadFile1);
        arrayList.add(uploadFile2);

        MemoryCreateFormDto memoryCreateFormDto = new MemoryCreateFormDto("오늘 식단", "성공적", 2L, arrayList);
        Memory memory = memoryCreateFormDto.toEntity();
        List<Photo> photolist = makePhotoEntity(arrayList,memory);

        //when
        when(photoRepository.findAllByMemoryId(memory.getMemoryId())).thenReturn(photolist);
        doNothing().when(photoRepository).deleteById(any());


        //then
        memoryService.deletePhoto(memory.getMemoryId());
        verify(photoRepository, times(2)).deleteById(any());
    }

    @DisplayName("memory get service TEST")
    @Test
    void getMemory() throws IOException{
        //given
        String path1 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image1.jpg";
        String path2 = "C:\\workplace\\Common_Project\\MyPage_CRUD\\backend\\src\\test\\java\\com\\ssafy\\kkini\\src\\Image2.png";
        MockMultipartFile uploadFile1 = new MockMultipartFile("file1", "Image1.jpg","image/jpg",new FileInputStream(new File(path1)));
        MockMultipartFile uploadFile2 = new MockMultipartFile("file2", "Image2.png","image/png",new FileInputStream(new File(path2)));

        List<MultipartFile> arrayList = new ArrayList<>();
        arrayList.add(uploadFile1);
        arrayList.add(uploadFile2);

        MemoryCreateFormDto memoryCreateFormDto1 = new MemoryCreateFormDto("오늘 식단1", "성공적", 1L, arrayList);
        MemoryCreateFormDto memoryCreateFormDto2 = new MemoryCreateFormDto("오늘 식단2", "성공적", 1L, arrayList);
        List<Memory> memoryList = new ArrayList<>();
        memoryList.add(memoryCreateFormDto1.toEntity());
        memoryList.add(memoryCreateFormDto2.toEntity());

        when(memoryRepository.FindAllByUserId(1L)).thenReturn(memoryList);

        //when
        List<Memory> result = memoryService.getMemory(1L);

        //then
        Assertions.assertThat(memoryList.size()).isEqualTo(result.size());

    }

    List<Photo> makePhotoEntity(List<MultipartFile> photoList, Memory memory){
        List<Photo> list = new ArrayList<Photo>();

        for (MultipartFile file:photoList) {
            Photo photo = new Photo();
            photo.setMemory(memory);
            photo.setFilePath("filename");
            photo.setOriginalFilename("originFileName");

            list.add(photo);
        }

        return list;
    }
}