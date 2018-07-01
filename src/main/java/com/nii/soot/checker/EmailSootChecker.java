package com.nii.soot.checker;


import com.nii.soot.core.BasicChecker;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.ValueBox;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author wzj
 * @create 2018-07-01 16:02
 **/
public class EmailSootChecker extends BasicChecker
{
    /**
     * 匹配email正则表达式
     */
    private String emailReg = "[\\w-.]+@[\\w-]+(.[\\w_-]+)+";

    /**
     * 正则表达式
     */
    private Pattern emailPattern = Pattern.compile(emailReg);

    /**
     * 检查APK应用中的url
     */
    public void checker()
    {
        //遍历应用中的每一个类
        for (SootClass sootClass : Scene.v().getApplicationClasses())
        {
            if (isExcludeClass(sootClass))
            {
                continue;
            }

            //遍历类中的每一个方法
            for (SootMethod sootMethod : sootClass.getMethods())
            {
                if (!sootMethod.hasActiveBody())
                {
                    continue;
                }

                //遍历方法中的每一行,检查email
                List<ValueBox> useBoxes = sootMethod.getActiveBody().getUseBoxes();
                for (ValueBox valueBox : useBoxes)
                {
                    String content = valueBox.toString();
                    Matcher matcher = emailPattern.matcher(content);
                    if (!matcher.find())
                    {
                        continue;
                    }

                    System.out.println("*********************************************");
                    System.out.println(matcher.group());
                    System.out.println(sootClass.getName());
                    System.out.println(sootMethod.getSubSignature());
                }
            }
        }
    }

    public static void main(String[] args)
    {
        new EmailSootChecker().analyze();
    }
}
