package example;
/** 
 * 给出一个数组，要怎么划分成2个数组使得2数组和之差最少<br/> 
 * 本质上就是，从数组中如何取数使其和等于某个target值，这里分割后的2个数组的平均值就是target值 
 * @author nizen 
 * 
 */  
public class ArrayCutting {  
  
    private int avg;  
      
    private int[][] k;  
      
    private void checkit(int[] array){  
        if (array == null || array.length==0) {  
            throw new IllegalArgumentException();  
        }  
    }  
    // 初始化定义target值和边界值  
    private void init(int[] array) {  
        int sum = 0;  
        for(int i=0;i<array.length;i++) {  
            sum += array[i];  
        }  
        avg = Math.round(sum / 2);  
          
        k = new int[avg+1][array.length+1];  
          
        for (int w=1; w<=avg; w++) {  
            for(int j=1; j<=array.length; j++) {  
                if (j==1){  
                    k[w][j]=getValueJ(array,j);  
                    continue;  
                }  
            }  
        }  
    }  
      
    public int[] cutit(int[] array) {  
        checkit(array);  
          
        init(array);  
          
                // 自底向上构造矩阵  
        for (int j=2; j<=array.length; j++) {  
            for (int w=1; w<=avg; w++) {  
                int valueAfterCutJ = w-getValueJ(array,j);  
                int lastJ = j-1;  
                  
                if (valueAfterCutJ == 0) {  
                    k[w][j] = getValueJ(array,j);   //选择J后差值为0则选择J为结果值  
                    continue;  
                }  
                int valueChooseJ = 0;  
                if (valueAfterCutJ < 0) {  
                    valueChooseJ = getValueJ(array, j); //期望值比J小则取J为选择J后的值  
                } else {  
                    valueChooseJ = k[valueAfterCutJ][lastJ] + getValueJ(array,j);  
                }  
                  
                if (Math.abs(k[w][lastJ]-w) < Math.abs(valueChooseJ-w)  ) {  
                    k[w][j]=k[w][lastJ];  
                } else {  
                    k[w][j]=valueChooseJ;  
                }  
            }  
        }  
          
        return findPath(array);  
    }  
      
        // 最后一步：构造出最优解  
    private int[] findPath(int[] array) {  
        int[] result = new int[array.length];  
        int p=0;  
        int j=array.length;  
        int w=avg;  
        while(j>0){  
            int valueAfterCutJ = w-getValueJ(array,j);  
            int lastJ = j-1;  
              
            if (valueAfterCutJ == 0) {  //清0跳出  
                result[p++]=getValueJ(array,j);  
                w=w-getValueJ(array,j);  
                break;  
            }  
            int valueChooseJ = 0;  
            if (valueAfterCutJ < 0) {  
                valueChooseJ = getValueJ(array, j); //期望值比J小则取J为选择J后的值  
            } else {  
                valueChooseJ = k[valueAfterCutJ][lastJ] + getValueJ(array,j);  
            }  
              
            if (Math.abs(k[w][lastJ]-w) > Math.abs(valueChooseJ-w)  ) {  
                result[p++]=getValueJ(array,j);  
                w=w-getValueJ(array,j);  
            }  
            j=j-1;  
        }  
        return result;  
    }  
  
    public static void main(String[] args) {  
        ArrayCutting ac = new ArrayCutting();  
        int[] r = ac.cutit(new int[]{87,54,51,7,1,12,32,15,65,78});  
        int selectedSum = 0;  
        for (int i=0;i<r.length;i++){  
            if (r[i]>0){  
                selectedSum +=r[i];  
                System.out.print(r[i]+"+");  
            }  
        }  
        System.out.println("="+selectedSum+" Target="+ac.avg);  
    }  
      
        // 返回第j个数组元素  
    private int getValueJ(int[]array, int j){  
        return array[j-1];  
    }  
}  