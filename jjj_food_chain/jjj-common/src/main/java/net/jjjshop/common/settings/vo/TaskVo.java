package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.config.properties.SpringBootJjjProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("任务中心VO")
public class TaskVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String backImage;
    private List<Task> growTask;
    private List<Task> dayTask;



    @Data
    @Accessors(chain = true)
    @ApiModel("任务VO")
    @AllArgsConstructor
    public static class Task implements Serializable{
        private static final long serialVersionUID = 1L;
        private String name;
        private String image;
        private Integer isOpen;
        private String taskType;
        private String rule;
        private Integer points;
        private Integer status;

        public Task(String name ,String image, String taskType ,String rule){
            this.name = name;
            this.image = image;
            this.isOpen = 1;
            this.taskType = taskType;
            this.rule = rule;
            this.points = 5;
            this.status = 0;
        }
    }

    public TaskVo(){
        String imagePath = SpringBootJjjProperties.getStaticAccessUrl();
        List<Task> growList = new ArrayList<>();
        growList.add(new Task("上传头像",imagePath+"image/task/image.png","image","首次修改头像奖励5积分"));
        growList.add(new Task("完善基本资料",imagePath+"image/task/base.png","base","首次修改昵称奖励5积分"));
        List<Task> dayList = new ArrayList<>();
        dayList.add(new Task("分享商品",imagePath+"image/task/product.png","product","当日分享商品给好友奖励5积分"));
        dayList.add(new Task("分享文章",imagePath+"image/task/article.png","article","当日分享文章给好友奖励5积分"));
        this.growTask = growList;
        this.dayTask = dayList;
        this.backImage = imagePath+"image/task/task.png";
    }

}
