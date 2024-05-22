package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.dto.LocationDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.BungaeMemberService;
import com.multi.bungae.service.BungaeService;
import com.multi.bungae.service.UserService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bungae")
public class BungaeController {

    private final BungaeService bungaeService;
    private final UserService userService;
    private final BungaeMemberService bungaeMemberService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BungaeMemberRepository bungaeMemberRepo;

    private static final Logger logger = LoggerFactory.getLogger(BungaeController.class);

    @GetMapping("/bungae_create")
    public String bungaeForm() {
        return "bungae_create";
    }

    /* @PostMapping("/create_bungae")
    public String createBungae(@ModelAttribute BungaeDTO bungaeDTO, @RequestParam double latitude, @RequestParam double longitude, HttpSession session) {
        
      Integer id = (Integer) session.getAttribute("loggedInId"); // userId(X), id(O)

        if (id == null) {
            return "redirect:/login";
        }

        UserVO user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Point location = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
        bungaeDTO.setBungaeLocation(location);
        bungaeService.createBungae(bungaeDTO, user); // 여기선 user table의 id

        return "redirection:/bungae/bungaeList";
        }
*/
    @PostMapping("/create_bungae")
    public ResponseEntity<Map<String, String>> createBungae(@ModelAttribute BungaeDTO bungaeDTO, @RequestParam("keyword") String keyword, @RequestParam("address") String address, @RequestParam("userId") String userId) {

        LocationDTO locationDTO = new LocationDTO(keyword, address);
        bungaeDTO.setBungaeLocation(locationDTO);
        UserVO user = userService.getUserByUserId(userId); // userId로 사용자 정보 조회
        bungaeService.createBungae(bungaeDTO, user);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("url", "/bungae_list");
        return ResponseEntity.ok(response);

    }

    /**
     * 번개 목록을 불러와서 json으로 바꿔서 반환
     */
    @RequestMapping(value = "/getList", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BungaeDTO> getList() {
        return bungaeService.bungaeList();
    }

    /**
     * bungaeList.html 호출
     */
    @GetMapping("/bungae_list")
    public String bungaeList() {
        return "bungae_list";
    }

    @GetMapping("/bungae_detail/{bungaeId}")
    public String bungaeDetail(@PathVariable Long bungaeId, Model model) {
        Bungae bungae = bungaeService.getBungaeById(bungaeId);
        model.addAttribute("bungae", bungae);
        return "bungae_detail";
    }

    @GetMapping(value = "/find/nearby", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BungaeDTO> findBungaeNearby(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        Point location = new GeometryFactory().createPoint(new Coordinate(lon, lat));
        return bungaeService.findBungaeNearby(location, radius);
    }

    @RequestMapping(value = "/getListOfStartTime", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BungaeDTO> getListOfStartTime() {
        return bungaeService.bungaeListOfStartTime();
    }

    @RequestMapping(value = "/getListOfCreateTime", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BungaeDTO> getListOfCreateTime() {
        return bungaeService.bungaeListOfCreateTime();
    }

    @GetMapping("/find/age")
    public Bungae findBungaeByAge(@RequestParam("age") int age) {
        return null;
    }

    /**
     * 수정, 삭제 주최자가 로그인 했을 때만 가능하게 수정해야함
     */
    @PutMapping("/{bungaeId}")
    public Bungae editBungae(@PathVariable Long bungaeId, @RequestBody BungaeDTO bungaeDTO, @RequestParam String userId) {
        UserVO user = userService.getUserByUserId(userId);
        return bungaeService.editBungae(bungaeId, bungaeDTO, user);
    }

    @DeleteMapping("/{bungaeId}")
    public String cancelBungae(@PathVariable Long bungaeId, @RequestParam String userId) {
        UserVO user = userService.getUserByUserId(userId);
        bungaeService.cancelBungae(bungaeId, user);
        return "redirect:/bungae/bungaeList";
    }
}
