package com.haoan.community.service;

import com.haoan.community.bean.Notification;
import com.haoan.community.bean.NotificationExample;
import com.haoan.community.bean.User;
import com.haoan.community.dto.NotificationDTO;
import com.haoan.community.dto.PageInfoDTO;
import com.haoan.community.enums.NotificationTypeEnum;
import com.haoan.community.exception.CustomizeErrorCode;
import com.haoan.community.exception.CustomizeException;
import com.haoan.community.mapper.NotificationMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;


    public PageInfoDTO list(Long id, Integer page, Integer size) {
        //对分页信息进行封装
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        Integer count = (int)notificationMapper.countByExample(notificationExample);
        pageInfoDTO.setPageInfo(count, page, size);

        //保证page不越界
        if (page < 1) {
            page = 1;
        }
        if (page > pageInfoDTO.getTotalPage()) {
            page = pageInfoDTO.getTotalPage();
        }

        //数据库查询当前页问题
        Integer offset = size * (page - 1);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        //查询未读信息放入DTO
        NotificationExample unreadNotificationExample = new NotificationExample();
        unreadNotificationExample.createCriteria()
                .andStatusEqualTo(0);
        unreadNotificationExample.setOrderByClause("gmt_create desc");
        List<Notification> unreadNotifications = notificationMapper.selectByExampleWithRowbounds(unreadNotificationExample,new RowBounds(offset,size));
        int readSize = unreadNotifications.size();
        for(Notification unreadNotification:unreadNotifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(unreadNotification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(unreadNotification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        //查询已读信息放入DTO
        NotificationExample readNotificationExample = new NotificationExample();
        readNotificationExample.createCriteria()
                .andStatusEqualTo(1);
        readNotificationExample.setOrderByClause("gmt_create desc");
        Long c = unreadCount(id);
        Long unreadPage = (c / size) + 1;
        Long yushu=c%size;
        if(unreadPage>=page){
            offset = 0;
        }else{
            offset= Math.toIntExact(size * (page - unreadPage - 1L) + size-yushu);
        }
        List<Notification> readNotifications = notificationMapper.selectByExampleWithRowbounds(readNotificationExample,new RowBounds(offset,size-readSize));
        for(Notification readNotification:readNotifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(readNotification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(readNotification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        pageInfoDTO.setData(notificationDTOS);
        return pageInfoDTO;
    }

    public Long unreadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(0);
        return notificationMapper.countByExample(notificationExample);
    }

    public Long read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification.getReceiver()!=user.getId()){
            new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(1);
        notificationMapper.updateByPrimaryKeySelective(notification);
        return notification.getOuterId();
    }
}
