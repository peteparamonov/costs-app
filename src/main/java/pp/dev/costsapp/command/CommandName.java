package pp.dev.costsapp.command;

import lombok.Getter;

@Getter
public enum CommandName {

    START("/start"),
    INCOME("/income"),
    SPEND("/spend"),
    WALLET("/wallet"),
    WALLETS("/wallets"),
    BALANCE("/balance"),
    BALANCES("/balances"),
    STOP("/stop"),
    NO("/no"),
    HELP("/help");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
