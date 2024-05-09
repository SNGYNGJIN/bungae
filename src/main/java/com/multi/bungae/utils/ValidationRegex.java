package com.multi.bungae.utils;

import com.multi.bungae.domain.User;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    // id 형식 체크
    public static boolean isRegexId(String target){
        String regex = "^[a-zA-Z0-9]{4,16}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 이메일 형식 체크
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 비밀번호 형식 체크
    public static boolean isRegexPassword(String target){
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 닉네임 형식 체크
    public static boolean isRegexNickname(String target){
        String regex = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 생년월일 형식 체크
    public static boolean isRegexBirthDate(String target){
        String regex = "^(19|20)[0-9]{2}-?(0[1-9]|1[0-2])-?(0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 이름 형식 체크
    public static boolean isRegexName(String target){
        String regex = "^[ㄱ-ㅎ가-힣]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 성별 형식 체크
    public static boolean isRegexGender(String target) {
        return Arrays.stream(User.Gender.values())
                .anyMatch(e -> e.name().equalsIgnoreCase(target));
    }

    // 전화번호 형식 체크
    public static boolean isRegexTel(String target){
        String regex = "^[a-zA-Z0-9]{4,16}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

}