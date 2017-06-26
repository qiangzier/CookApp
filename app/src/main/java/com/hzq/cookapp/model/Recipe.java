package com.hzq.cookapp.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/6/22
 * @version:
 * @description: 制作步骤
 */

public class Recipe implements Serializable{
    private static final long serialVersionUID = -4889066983586955578L;

    public String img;          //http://f2.mob.com/null/2015/08/18/1439876711867.jpg",
    public String ingredients;  //[\"虾仁350g、油1大勺、盐1小勺、白酒1小勺、淀粉2小勺、黄瓜30g、胡萝卜30g、玉米粒30g\"]",
    private List<String> ingredientsData;
    public String method;
    private List<Method> methodList;       //[{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876712533.jpg\",\"step\":\"1.虾仁清洗干净，去掉虾线\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876713007.jpg\",\"step\":\"2.加入1小勺盐\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876713790.jpg\",\"step\":\"3.加入1小勺白酒拌匀，腌制15分钟。\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876715218.jpg\",\"step\":\"4.腌制好的虾仁加入2小勺淀粉抓拌均匀\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876715861.jpg\",\"step\":\"5.胡萝卜和黄瓜洗净切小丁。\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876716477.jpg\",\"step\":\"6.锅中加入1大勺油，油热后下锅滑炒虾仁，虾仁变色后即可捞出\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876717026.jpg\",\"step\":\"7.另起锅加入少许油，将胡萝卜和黄瓜丁下锅炒至断生\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876717929.jpg\",\"step\":\"8.加入少许盐，加入玉米粒翻炒\"},{\"img\":\"http://f2.mob.com/null/2015/08/18/1439876718385.jpg\",\"step\":\"9.然后加入事先炒好的虾仁，翻炒均匀，即可关火，一道美味的虾仁就出来啦\"}]",
    public String sumary;       //鲜虾中含有丰富的钾、碘、镁、磷等矿物质及多种维生素，尤其是它特有的虾青素，是目前发现的最强的一种抗氧化剂，所以虾是对人体十分有益的食材。这是一道以虾仁为主要材料炒制的菜肴。炒虾仁因其清淡爽口，易于消化，老幼皆宜，而深受食客欢迎，它的配料可以随个人喜好而变化，做法多样，我今天就加入了玉米、胡萝卜和黄瓜，不但颜色好看，而且口感丰富，一看就让人食欲大开，还能摄取多种营养。",
    public String title;    //怎样做一道好吃的三色炒虾仁"

    public List<String> getIngredientsData() {
        if(!TextUtils.isEmpty(ingredients)){
            String str = ingredients.replace("\\","");
            ingredientsData = new Gson().fromJson(str,new TypeToken<List<String>>(){}.getType());
        }else{
            ingredientsData = new ArrayList<>();
        }
        return ingredientsData;
    }

    public List<Method> getMethodList() {
        if(!TextUtils.isEmpty(method)){
            String str = method.replace("\\","");
            methodList = new Gson().fromJson(str,new TypeToken<List<Method>>(){}.getType());
        }else{
            methodList = new ArrayList<>();
        }
        return methodList;
    }

    public static class Method implements Serializable{
        private static final long serialVersionUID = 6150725908936061984L;
        public String img;
        public String step;
    }
}
