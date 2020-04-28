package com.haoan.community.controller;

import com.haoan.community.bean.User;
import com.haoan.community.dto.AccessTokenDTO;
import com.haoan.community.dto.GithubUser;
import com.haoan.community.mapper.UserMapper;
import com.haoan.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class GithubController {

    @Autowired
    GithubProvider githubProvider;

    @Value("${github.Client_id}")
    private String Client_id;

    @Value("${github.Redirect_uri}")
    private String  Redirect_uri;

    @Value("${github.Client_secret}")
    private String  Client_secret;

    @Autowired
    UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setClient_secret(Client_secret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);


        if (githubUser!=null){

//            User user = new User();
//
//            user.setToken(UUID.randomUUID().toString());
//            user.setAccount_id(String.valueOf(githubUser.getId()));
//            user.setName(githubUser.getName());
//            user.setGmt_create(System.currentTimeMillis());
//            user.setGmt_modfied(user.getGmt_create());
//            userMapper.saveUser(user);
//
//            request.getSession().setAttribute("user",githubUser);
//            return "redirect:/";


            if(userMapper.findById(String.valueOf(githubUser.getId()))==null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modfied(user.getGmt_create());
            user.setAvatar_url(githubUser.getAvatar_url());

            userMapper.saveUser(user);

            request.getSession().setAttribute("user",user);
            return "redirect:/";
            }else {
                User user = userMapper.findById(String.valueOf(githubUser.getId()));
                request.getSession().setAttribute("user",user);
                return "redirect:/";
            }
        }else {
            //登录失败
            return "redirect:/";
        }


    }

    @GetMapping("/login")
    public String login(@RequestParam("name") String name,
                        HttpServletRequest request){
        User test = userMapper.test(name);
        request.getSession().setAttribute("user",test);
        return "redirect:/";
    }
}