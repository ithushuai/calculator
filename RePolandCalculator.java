package stack;

import java.util.ArrayList;
import java.util.Stack;

public class RePolandCalculator {
    public static double operation(String expr) {//10 + （2+3）*2 - 1   10 2 3 + 2 * 1 - +
        //1.将表达式转成后缀表达式
        //1.1需要将表达式分解成数和运算符，并存入ArrayList,得到中缀表达式
        ArrayList<String> exprList = getOpList(expr);
        System.out.println(exprList);
        //1.2.初始化一个栈，用于存运算符，一个ArrayList用于存中间结果,得到后缀（逆波兰）表达式
        ArrayList<String> resList = getSuffixList(exprList);
        System.out.println(resList);
        //2.最终的resList中按顺序排列就是后缀表达式，扫描计算后缀表达式
        Stack<Double> calStack = calcSuffixList(resList);
        return calStack.pop();
    }

    /**
     * 计算后缀（逆波兰）表达式
     *
     * @param resList
     * @return
     */
    private static Stack<Double> calcSuffixList(ArrayList<String> resList) {
        //创建一个栈，用于存储数据
        Stack<Double> calStack = new Stack<>();
        for (String item : resList) {
            if (item.matches("\\d+|(\\d+\\.\\d+)")) {
                calStack.push(Double.valueOf(item));
            } else {//如果是运算符，则将栈顶元素和次顶元素弹出进行计算，然后将结果再入栈
                double num1 = calStack.pop();
                double num2 = calStack.pop();
                double result = 0.0;
                switch (item) {
                    case "+":
                        result = num2 + num1;
                        break;
                    case "-":
                        result = num2 - num1;
                        break;
                    case "*":
                        result = num2 * num1;
                        break;
                    case "/":
                        result = num2 / num1;
                        break;
                    default:
                        break;
                }
                //将计算结果入栈
                calStack.push(result);
            }
        }
        return calStack;
    }

    /**
     * 将中缀表达式转成后缀（逆波兰）表达式
     *
     * @param exprList
     * @return
     */
    private static ArrayList<String> getSuffixList(ArrayList<String> exprList) {//[10, +, (, (, 2.1, +, 3, ), *, 2, +, (, 1.1, +, 2, *, 3, ), ), *, 2]
        Stack<String> opStack = new Stack<>();
        ArrayList<String> resList = new ArrayList<>();
        for (String item : exprList) {//从左到右扫描中缀表达式
            if (item.matches("\\d+|(\\d+\\.\\d+)")) {//如果是数，则存入中间结果resList
                resList.add(item);
            } else if ("(".equals(item)) {//如果是左括号，直接压入符号栈
                opStack.push(item);
            } else if (")".equals(item)) {//如果是右括号，依次将符号栈中的符号弹出并存入到结果集合中，直到遇到左括号(
                while (!"(".equals(opStack.peek())) {
                    resList.add(opStack.pop());
                }
                opStack.pop();//再将栈顶的（弹出
            } else {//是运算符
                //如果当前运算符的优先级比栈顶符号优先级低，则栈顶的符号弹出，直到栈顶符号优先级不再比当前符号高
                while (!opStack.empty() && getPriority(opStack.peek()) > getPriority(item)){
                    resList.add(opStack.pop());
                }
                opStack.push(item);//将符号压入栈
            }
        }
        //扫描结束后，将符号栈中的符号依次弹出并存入结果集合
        while (!opStack.empty()) {
            resList.add(opStack.pop());
        }
        return resList;
    }

    /**
     * 将运算表达式拆分成中缀表达式
     *
     * @param expr
     * @return
     */
    private static ArrayList<String> getOpList(String expr) {
        ArrayList<String> opList = new ArrayList<>();
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) < 48 || expr.charAt(i) > 57) {//如果不是数字，直接存
                opList.add(String.valueOf(expr.charAt(i)));
            } else {//如果是数字，考虑多位数和小数点
                int j = 1;
                while ((i + j) < expr.length() && (expr.charAt(i + j) >= 48 && expr.charAt(i + j) <= 57 || expr.charAt(i + j)=='.')) {
                    j++;
                }
                opList.add(expr.substring(i, i + j));
                i = i + j - 1;
            }
        }
        return opList;
    }

    /**
     * 获取运算符的优先级
     *
     * @param op 运算符
     * @return
     */
    private static int getPriority(String op) {
        if ("+".equals(op) || "-".equals(op)) {
            return 0;
        } else if ("*".equals(op) || "/".equals(op)) {
            return 1;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        double result = operation("10+((2.1+3)*2+(1.1+2*3))*2");
        System.out.println(result);
    }
}
