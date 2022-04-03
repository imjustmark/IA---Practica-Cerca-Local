package src;

import aima.search.framework.GoalTest;

public class EstadoFinal implements GoalTest {
    public boolean isGoalState(Object aState) {
        RescateEstado estat = (RescateEstado) aState;
        return (estat.isGoalState());
    }
}
