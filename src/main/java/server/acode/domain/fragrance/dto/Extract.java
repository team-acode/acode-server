package server.acode.domain.fragrance.dto;

import java.util.ArrayList;
import java.util.List;

public class Extract {
    private List<Long> one;
    private List<Long> two;
    private List<Long> three;
    private List<Long> fourAnd;
    private List<Long> fourOr;
    private List<Long> fiveAnd;
    private List<Long> fiveOr;

    public Extract(List<Long> one, List<Long> two, List<Long> three, List<Long> fourAnd, List<Long> fourOr,
                List<Long> fiveAnd, List<Long> fiveOr){
        this.one = one;
        this.two = two;
        this.three = three;
        this.fourAnd = fourAnd;
        this.fourOr = fourOr;
        this.fiveAnd = fiveAnd;
        this.fiveOr = fiveOr;
    }

    public List<Long> dfs(int idx, List<Long> now){
        List<Long> result = new ArrayList<>(now);

        // 다섯번째 노드 도달
        if(idx == 5){
            result.retainAll(fiveAnd);
            if(result.isEmpty()) {
                result = new ArrayList<>(now);
                result.retainAll(fiveOr);
                if(result.isEmpty()){
                    return null; // 해당하는 값 없음
                }
                return result; // OR 결과 있음
            }
            else{
                return result; } // AND 결과 있음
        }

        result.retainAll(relevant(idx));

        // 4번째 노드는 AND 포함 결과 없을 때 OR 확인 필요
        if(idx == 4 && result.isEmpty()){
            result = new ArrayList<>(now);
            result.retainAll(relevant(5));
        }



        if(!result.isEmpty()) {
            for (int i = idx + 1; i <= 5; i++) {
                List<Long> newResult = dfs(i, result);

                // 아래 노드에서 다 포함하는 결과가 있다면 그걸로 리턴
                if(newResult!=null) {
                    return newResult;
                }
            }
            return result;
        }
        return null; // 해당하는 값 없음
    }

    private List<Long> relevant(int idx){
        switch (idx){
            case 1: return one;
            case 2: return two;
            case 3: return three;
            case 4: return fourAnd;
            case 5: return fourOr;
        }
        return null;
    }


}
