package gui;

import java.util.Vector;

public class AI_1 extends AI_method {
    private int x;
    private int y;

    private Board_state state[][];
    private int[][] chessArray;


    //private int playerId ;

    AI_1() {
    }

    @Override
    void setBoardstates(Board_state[][] boardstates) {
        super.setBoardstates(boardstates);
    }

    @Override
    public Vector<Integer> getxy() {
        randomAxisOfTurn();
        Vector<Integer> vector = new Vector<>();
        vector.add(this.x);
        vector.add(this.y);
        return vector;
    }

    //计算三边格子的空边
    public boolean threeLinesOfBox() {
        for (int i = 0; i < chessArray.length; i++) {
            for (int j = 0; j < chessArray[i].length; j++) {
                if (chessArray[i][j] == 3) {
                    if (chessArray[i + 1][j] == 1) {
                        this.x = i + 1;
                        this.y = j;
                    } else if (chessArray[i - 1][j] == 1) {
                        this.x = i - 1;
                        this.y = j;
                    } else if (chessArray[i][j + 1] == 1) {
                        this.x = i;
                        this.y = j + 1;
                    } else {
                        this.x = i;
                        this.y = j - 1;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    //获得X值
    public int getX() {
        return x;
    }

    //获得y值
    public int getY() {
        return y;
    }

    //随机产生边的坐标,没有三边格子且已经进行30步以内则随机产生边若产生 返回true 否则 返回false
/*	public boolean randomAxis(){
		int count = 0 ;
		if(!threeLinesOfBox()){
			for(int i=0;i<chessArray.length;i++){
				for(int j=0;j<chessArray[i].length;j++){
					if(chessArray[i][j]==0&&( (i%2==1&&j%2==0) || (i%2==0&&j%2==1) )){
						count++ ;
					}
				}
			}
			if(count<30){
				boolean flag = false;
				do{
					this.x = (int) ( 1 +  Math.random() * (10 - 1 + 1) );
					this.y = (int) ( 1 +  Math.random() * (10 - 1 + 1) );
					if(chessArray[x][y]==1 && ( (x%2==1&&y%2==0) || (x%2==0&&y%2==1) )){
						flag = true ;
					}
				}while(!flag);
				return true ;
			}else {return false ;}

		}else {return false ;}
	}
*/
    //计算剩余边
    public int remainLinesNum() {
        int linesNum = 0;
        for (int i = 0; i < chessArray.length; i++) {
            for (int j = 0; j < chessArray[i].length; j++) {
                if (chessArray[i][j] == 0 && ((i % 2 == 1 && j % 2 == 0) || (i % 2 == 0 && j % 2 == 1))) {
                    linesNum++;
                }
            }
        }
        return linesNum;
    }

    //轮盘法产生随机边 随机数=(数据类型)(最小值+Math.random()*(最大值-最小值+1))
    public void randomAxisOfTurn() {
        int linesNum = 0;
        int count = 0;
        int a = 0;
        if (!threeLinesOfBox()) {
            linesNum = remainLinesNum();
            count = (int) (0 + Math.random() * (linesNum - 0 + 1));
            for (int i = 0; i < chessArray.length; i++) {
                for (int j = 0; j < chessArray[i].length; j++) {
                    if (chessArray[i][j] == 1 && ((i % 2 == 1 && j % 2 == 0) || (i % 2 == 0 && j % 2 == 1))) {
                        a++;
                        if (a == count) {
                            this.x = i;
                            this.y = j;
                            break;
                        }
                    }
                }
            }
        }
    }


}
