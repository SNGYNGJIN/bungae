package com.multi.bungae.controller.user;

import com.multi.bungae.config.BaseException;
import com.multi.bungae.config.BaseExceptionStatus;
import com.multi.bungae.config.BaseResponse;
import com.multi.bungae.domain.BlackList;
import com.multi.bungae.domain.UserProfile;
import com.multi.bungae.domain.UserReview;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.OfflineDTO;
import com.multi.bungae.service.UserService;
import com.multi.bungae.dto.user.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
public class UserApiController {

    @Autowired
    private final UserService userService;

    /*
        로그인 API
    */
    @PostMapping("/login")
    @ResponseBody
    public BaseResponse<LoginRes> login(@RequestBody LoginReq loginReq, HttpSession session) throws BaseException {
        LoginRes loginRes = userService.login(loginReq, session);
        return new BaseResponse<>(loginRes);
    }


    /*
        중복 아이디 체크 API
     */
    @PostMapping("/check_id")
    @ResponseBody
    public BaseResponse<CheckIdRes> checkId(@RequestBody CheckIdReq checkIdReq) {
        CheckIdRes checkIdRes = userService.checkId(checkIdReq);
        return new BaseResponse<>(checkIdRes);
    }

    /*
        회원가입 API
     */
    @PostMapping("/signup")
    @ResponseBody
    public BaseResponse<SignupRes> signup(@RequestBody SignupReq signupReq) throws BaseException {
        log.info("signupReq: {}", signupReq);
        SignupRes signupRes = userService.signupRes(signupReq);
        return new BaseResponse<>(signupRes);
    }

    /*
        아이디 찾기 API
     */
    @PostMapping("/find_id")
    @ResponseBody
    public BaseResponse<FindIdRes> findId(@RequestBody FindIdReq findIdReq) throws BaseException {
        FindIdRes findIdRes = userService.findId(findIdReq);
        return new BaseResponse<>(findIdRes);
    }

    /*
        userID를 통해 user 테이블의 모든 정보를 가져오는 API
     */
    @GetMapping("/info/{userId}")
    @ResponseBody
    public BaseResponse<UserVO> getUserInfo(@PathVariable String userId) throws BaseException{
        UserVO user = userService.getUserByUserId(userId);
        return new BaseResponse<>(user);
    }

    /*
        id를 통해 user_profile 테이블의 모든 정보를 가져오는 API
     */
    @GetMapping("/info/profile/{id}")
    @ResponseBody
    public BaseResponse<UserProfileDTO> getUserProfileDtoById(@PathVariable int id) throws BaseException{
        UserProfileDTO user = userService.getUserProfileDtoById(id);
        return new BaseResponse<>(user);
    }

    /*
        자기소개, 프로필 사진, 닉네임의 수정사항을 업데이트하는 API
     */
    @PostMapping("/updateProfile/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable String userId,
                                           @RequestParam(value = "nickname", required = false) String nickname,
                                           @RequestParam(value = "userInfo", required = false) String userInfo,
                                           @RequestParam(value = "imageUpload", required = false) MultipartFile file) {
        try {
            UserProfile userProfile = userService.getUserProfileByUserId(userId);

            if (userProfile == null) {
                return ResponseEntity.notFound().build();
            }

            if (nickname != null && !nickname.isEmpty()) {
                userProfile.getUser().setNickname(nickname);
            }
            if (userInfo != null && !userInfo.isEmpty()) {
                userProfile.setUserInfo(userInfo);
            }

            String filename = "";
            if (file != null && !file.isEmpty()) {
                filename = storeFile(file);
                userProfile.setUserImage(filename); // 파일 경로 저장
            }

            UserProfileDTO up = userService.saveUserProfile(userProfile);

            if (!filename.isEmpty()) {
                int index = filename.lastIndexOf("/") + 1;
                String fname = filename.substring(index); // 마지막 파일명만 추출
                up.setUserImage(fname);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(up);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating profile"));
        }
    }


    /* 파일 저장 */
    private String storeFile(MultipartFile file) throws Exception {
        String extension = getFileExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + "." + extension;
        Path storageDirectory = Paths.get(System.getProperty("user.dir"),"uploads");
        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory);
        }
        Path destinationPath = storageDirectory.resolve(filename);
        try{
            file.transferTo(destinationPath);
            System.out.println("업로드 성공");
        }catch(Exception e){
            e.printStackTrace();
        }
        filename = "/uploads/" + filename;
        return filename;
    }
    /* 파일 확장자 추출 */
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /*
        <사진 삭제> 버튼을 통해 UserImage를 기본 이미지로 수정하는 API
     */
    @PostMapping("/deleteProfileImage/{userId}")
    public ResponseEntity<?> deleteProfileImage(@PathVariable String userId, @RequestBody Map<String, String> body) {
        try {
            UserProfile userProfile = userService.getUserProfileByUserId(userId);
            if (userProfile == null) {
                return ResponseEntity.notFound().build();
            }

            String defaultImageSrc = body.get("userImage");
            userProfile.setUserImage(defaultImageSrc);
            userService.saveUserProfile(userProfile); // 변경된 userProfile 저장

            return ResponseEntity.ok().body(Map.of("message", "프로필 이미지 삭제 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "프로필 이미지 삭제 실패"));
        }
    }

    /*
        userID를 통해 blacklist 테이블의 모든 블랙리스트 항목을 가져오는 API
    */
    @GetMapping("/info/black/{userId}")
    @ResponseBody
    public BaseResponse<List<BlackList>> getUserBlacklists(@PathVariable String userId) throws BaseException{
        List<BlackList> blackLists = userService.getUserBlacklist(userId);
        return new BaseResponse<>(blackLists);
    }

    @PostMapping("/offline/{userId}")
    public ResponseEntity<String> updateOfflineState(@PathVariable String userId, @RequestBody OfflineDTO dto) {
        System.out.println("Received request to update state for user " + userId + " with state " + dto.getState());
        try {
            userService.updateOfflineState(userId, dto);
            return new ResponseEntity<>("State updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update state", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
