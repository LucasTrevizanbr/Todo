package br.com.todo.domain.model.enums;

public enum Difficulty {
    EASY {
        @Override
        public int getPoints() {
            return 2;
        }
    },
    MEDIUM {
        @Override
        public int getPoints() {
            return 4;
        }
    },
    HARD {
        @Override
        public int getPoints() {
            return 6;
        }
    };

    public abstract int getPoints();
}
