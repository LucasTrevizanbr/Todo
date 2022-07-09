package br.com.todo.domain.service.score.strategy;



public enum ScoreValues {

    PER_DAY{
        @Override
        public double getValue(){
            return 1.0;
        }
    },
    PER_DAY_PENALTY{
        @Override
        public double getValue(){
            return 0.5;
        }
    };

    public abstract double getValue();
}
