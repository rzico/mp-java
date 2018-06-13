package net.wit.liveRobot;

import net.wit.Filter;
import net.wit.Page;
import net.wit.controller.model.live.UserInfo;
import net.wit.entity.Live;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.plat.im.Push;
import net.wit.service.LiveService;
import net.wit.service.MemberService;
import net.wit.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Robot {


    private MemberService memberService;

    private LiveService liveService;

    private  List<Member> robots;

    public static Robot robot;
    public enum Type{
        CustomTextMsg//文本消息
    }
    public Robot(MemberService memberService, LiveService liveService){
        this.memberService = memberService;
        this.liveService = liveService;
    }

    public static Robot create(MemberService memberService, LiveService liveService){
        if(robot == null){
            return robot = new Robot(memberService, liveService);
        }else {
            return robot;
        }
    }

    private long time = 3000; //每隔几秒加机器人
    /**
     * 机器人启动
     */
    public  void joinRobot(final String groupId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(time);//睡眠1000毫秒
                }catch(InterruptedException e){e.printStackTrace();}
                for(int i=0;i<10;i++){

                    Member member  = getRobot();
                    if(member != null){
                        try {
                            //向直播间发送消息
                            Push.impushgroup(groupId, bindRobot(member, "加入房间", Type.CustomTextMsg));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    try{
                        Thread.sleep(time);//睡眠1000毫秒
                    }catch(InterruptedException e){e.printStackTrace();}
                }
            }
        }).start();
    }

    private UserInfo bindRobot(Member member, String send, Type type){
        UserInfo userInfo = new UserInfo();
        userInfo.id = member.getId();
        userInfo.nickName = member.getNickName();
        userInfo.headPic = member.getLogo();
        userInfo.text = send;
        userInfo.cmd = type.name();
        return userInfo;
    }

    private List<Live> getPlayLive(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("online", Filter.Operator.eq, "1"));
        return liveService.findList(null, filters, null);
    }
    /**
     * 随机获取机器人
     * @return
     */
    private Member getRobot(){
        if(robots == null){
//            Member
            Long robotCount = memberService.getRobotCount();
            if(robotCount != null){
                List<Filter> filters = new ArrayList<>();
                filters.add(new Filter("userType", Filter.Operator.eq, Member.UserType.ROBOT));
                List<net.wit.Order> orders = new ArrayList<>();
                robots = memberService.findList(robotCount.intValue(), filters, orders);
                if(robots != null && robots.size()>0){
                    int ramdon = StringUtils.randomNum(0, robots.size() - 1);
                    return robots.get(ramdon);
                }
            }
        }else {
            int ramdon = StringUtils.randomNum(0, robots.size() - 1);
            return robots.get(ramdon);
        }
        return null;
    }
}
