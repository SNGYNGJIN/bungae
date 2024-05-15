package com.multi.bungae.controller;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeMember;
import com.multi.bungae.domain.UserVO;
import com.multi.bungae.dto.BungaeDTO;
import com.multi.bungae.dto.BungaeMemberDTO;
import com.multi.bungae.service.BungaeMemberService;
import com.multi.bungae.service.BungaeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bungae")
public class BungaeController {

    private final BungaeService bungaeService;
    private final BungaeMemberService bungaeMemberService;

    private static final Logger logger = LoggerFactory.getLogger(BungaeController.class);

    @GetMapping("/bungaeForm")
    public String bungaeForm() {
        return "bungaeForm";
    }

    @PostMapping("/create_bungae")
    public String createBungae(@ModelAttribute BungaeDTO bungaeDTO, @RequestParam double latitude, @RequestParam double longitude, HttpSession session) {

//        UserVO user = (UserVO) session.getAttribute("loggedInUser"); // 로그인된 유저 연결
//        Bungae bungae = bungaeService.createBungae(bungaeDTO, user);
        Point location = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
        bungaeDTO.setBungaeLocation(location);
        Bungae bungae = bungaeService.createBungae(bungaeDTO);
        logger.info("bungae: " + bungae);

        return "redirect:/bungae/bungaeList";
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
    @GetMapping("/bungaeList")
    public String bungaeList() {
        return "bungaeList";
    }

    @GetMapping(value = "/find/nearby", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BungaeDTO> findBungaeNearby(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        Point location = new GeometryFactory().createPoint(new Coordinate(lon, lat));
        return bungaeService.findBungaeNearby(location, radius);
    }

    @GetMapping("/find/type")
    public Bungae findBungaeByType(@RequestParam("type") String bungaeType) {
        return null;
    }

    @GetMapping("/find/age")
    public Bungae findBungaeByAge(@RequestParam("age") int age) {
        return null;
    }

    /**
     * 수정, 삭제 주최자가 로그인 했을 때만 가능하게 수정해야함
     */
    @PutMapping("/{bungaeId}")
    public Bungae editBungae(/*주최자일때만*/@PathVariable Long bungaeId, @RequestBody BungaeDTO bungaeDTO) {
        return bungaeService.editBungae(bungaeId, bungaeDTO);
    }

    @DeleteMapping("/{bungaeId}")
    public String cancelBungae(/*주최자일때만*/@PathVariable Long bungaeId) {
        bungaeService.cancelBungae(bungaeId);
        return "redirect:/bungae/bungaeList";
    }
}
