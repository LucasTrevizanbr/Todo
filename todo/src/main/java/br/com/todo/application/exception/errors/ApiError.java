package br.com.todo.application.exception.errors;

public enum ApiError {

    TG001{
        @Override
        public String getMessageError() {
            return "Resource not found";
        }
    },
    TG002{
        @Override
        public String getMessageError() {
            return "Invalid arguments";
        }
    },

    TG101{
        @Override
        public String getMessageError() {
            return "You can't finish a stopped goal";
        }
    },
    TG102{
        @Override
        public String getMessageError() {
            return "This goal has unfinished tasks";
        }
    };

    public abstract String getMessageError();
}
