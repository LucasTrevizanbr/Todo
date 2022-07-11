package br.com.todo.domain.service.score.strategy;



public enum ScoreValues {

    POINTS_PER_DAY {
        @Override
        public double getValue(){
            return 1.0;
        }
    },
    POINTS_PER_DAY_PENALTY {
        @Override
        public double getValue(){
            return 0.5;
        }
    },
    POINTS_PER_DAY_BONUS {
        @Override
        public double getValue() {
            return 1.5;
        }
    };

    public abstract double getValue();
}
