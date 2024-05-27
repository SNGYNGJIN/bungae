package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.BungaeType;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.dto.LocationDTO;
import com.multi.bungae.repository.BungaeMemberRepository;
import com.multi.bungae.repository.UserRepository;
import com.multi.bungae.service.BungaeMemberService;
import com.multi.bungae.service.BungaeService;
import com.multi.bungae.service.UserService;
import jakarta.servlet.http.HttpSession;
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
import java.util.Optional;

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

    @RequestMapping(value = "/getList", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BungaeDTO> getList() {
        return bungaeService.bungaeList();
    }

    @GetMapping("/bungae_update/{bungaeId}")
    public String updateBungaeForm(@PathVariable Long bungaeId, Model model) {
        Bungae bungae = bungaeService.getBungaeById(bungaeId);
        model.addAttribute("bungae", bungae);
        return "bungae_update";
    }

    @GetMapping("/bungae_list")
    public String bungaeList() {
        return "bungae_list";
    }

    @GetMapping("/bungae_detail/{bungaeId}")
    public String bungaeDetail(@PathVariable Long bungaeId, HttpSession session, Model model) {
        Bungae bungae = bungaeService.getBungaeById(bungaeId);
        String bungaeTypeKorean = convertBungaeTypeToKorean(bungae.getBungaeType());
        int currentMemberCount = bungaeMemberService.countByBungae_BungaeId(bungae.getBungaeId());

        String userId = (String) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<BungaeMember> organizerOptional = bungaeMemberService.getOrganizerByBungaeId(bungaeId);
        if (organizerOptional.isPresent()) {
            boolean isOrganizer = organizerOptional.get().getUser().getUserId().equals(userId);
            UserVO organizer = organizerOptional.get().getUser();
            model.addAttribute("organizer", organizer);
            model.addAttribute("isOrganizer", isOrganizer);
        }

        model.addAttribute("bungae", bungae);
        model.addAttribute("currentMemberCount", currentMemberCount);
        model.addAttribute("bungaeTypeKorean", bungaeTypeKorean);

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

    @PostMapping("/update_bungae")
    public ResponseEntity<Map<String, String>> updateBungae(@ModelAttribute BungaeDTO bungaeDTO, @RequestParam("keyword") String keyword, @RequestParam("address") String address) {
        LocationDTO locationDTO = new LocationDTO(keyword, address);
        bungaeDTO.setBungaeLocation(locationDTO);
        bungaeService.updateBungae(bungaeDTO.getBungaeId(), bungaeDTO);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("url", "/bungae_list");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bungaeId}/cancel")
    public ResponseEntity<Map<String, String>> cancelBungae(@PathVariable Long bungaeId, HttpSession session) {
        String userId = (String) session.getAttribute("loggedInUserId");
        UserVO user = userService.getUserByUserId(userId);
        bungaeService.cancelBungae(bungaeId, user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation successful");
        response.put("redirectUrl", "/bungae_list");

        return ResponseEntity.ok(response);
    }

    private String convertBungaeTypeToKorean(BungaeType bungaeType) {
        switch (bungaeType) {
            case DRINK:
                return "술자리";
            case SOCCER:
                return "축구";
            case BASKETBALL:
                return "농구";
            case CYCLE:
                return "자전거";
            case RUNNING:
                return "러닝";
            case STUDY:
                return "공부";
            default:
                return "알 수 없음";
        }
    }
}
