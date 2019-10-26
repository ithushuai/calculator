package stack;

import java.util.Stack;

public class StackCalculator {
    /**
     * 混合运算
     * @param expr
     * @return
     */
    private static double operation(String expr) {
        //创建两个栈，一个栈存数，另一个栈存符号
        Stack<Double> stack1 = new Stack<>();
        Stack<Character> stack2 = new Stack<>();
        //依次将数和符号入栈
        for (int i = 0; i < expr.length(); i++) {
            //依次取出字符串中的数，要考虑是否是多位数
            if (expr.charAt(i) >= 48 && expr.charAt(i) <= 57) {//遇到数字，则继续往后找字符，如果仍然是数字或者'.'，则拼接成一个数
                int j = 1;
                while ((i + j) < expr.length() && (expr.charAt(i + j) >= 48 && expr.charAt(i + j) <= 57 || expr.charAt(i + j)=='.')) {
                    j++;
                }
                double num = Double.valueOf(expr.substring(i, i + j));
                stack1.push(num);
                i = i + j - 1;
            } else {//遇到符号
                char ch = expr.charAt(i);
                //如果遇到左括号
                if(ch == '('){
                    int countBracket = 0;//定义一个计数器来表示遇到的左括号个数
                    for (int j = i; j < expr.length(); j++) {//从左括号的索引开始，再遍历表达式
                        if(expr.charAt(j) == '('){//如果遇到左括号，计数器+1
                            countBracket++;
                        }
                        if(expr.charAt(j) == ')'){//如果遇到右括号，计数器-1
                            countBracket--;
                        }
                        if(countBracket == 0){//当计数器为0时，当前索引就是与左括号对应的右括号
                            stack1.push(operation(expr.substring(i + 1, j)));//递归求出括号内的值
                            i = j;//结束之后，直接跳过括号
                            break;//不再遍历
                        }
                    }
                    continue;//处理完括号后，直接跳过本次循环，进行后面代码的判断
                }
                //如果是运算符，判断符号栈中最顶部的符号和要入栈的符号的优先级
                if (!stack2.isEmpty() && priority(ch) < priority(stack2.peek())) {//如果要入栈的符号比栈顶符号优先级小
                    //需要将数栈中的最上边两个数取出，进行运算
                    double num1 = stack1.pop();
                    double num2 = stack1.pop();
                    double result = calc(num2, num1, stack2.pop());//注意，num2要在num1前面
                    stack1.push(result);//计算结果入栈
                    stack2.push(ch);//当前符号入栈
                } else {
                    stack2.push(ch); //如果要入栈的符号比栈顶符号优先级大，则入栈
                }
            }
        }
        //所有数和符号都入栈完毕，从栈顶取数据进行运算
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            double num1 = stack1.pop();
            double num2 = stack1.pop();
            double result = calc(num2, num1, stack2.pop());
            stack1.push(result);
        }
        //计算完毕之后，数栈中仅剩一个数，就是最后的结果
        return stack1.pop();
    }

    /**
     * 给出符号的优先级，//乘除2，加减1
     *
     * @param ch
     * @return
     */
    private static int priority(char ch) {
        if (ch == '*' || ch == '/') {
            return 1;
        } else if (ch == '+' || ch == '-') {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 两个数进行四则运算
     *
     * @param num1
     * @param num2
     * @return
     */
    private static double calc(double num1, double num2, char ch) {
        double result = 0;
        switch (ch) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            default:
                throw new RuntimeException("符号不正确");
        }
        return result;
    }

    public static void main(String[] args) {
        double result = operation("10+((2.1+3)*2+(1.1+2*3))*2");
        System.out.println(result);
    }
}
