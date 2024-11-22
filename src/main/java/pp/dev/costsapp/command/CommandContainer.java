package pp.dev.costsapp.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static pp.dev.costsapp.command.CommandName.HELP;
import static pp.dev.costsapp.command.CommandName.INCOME;
import static pp.dev.costsapp.command.CommandName.NO;
import static pp.dev.costsapp.command.CommandName.START;
import static pp.dev.costsapp.command.CommandName.STOP;

@RequiredArgsConstructor
@Component
public class CommandContainer {

    public static final String HELP_MESSAGE = String.format("✨<b>Доcтупные команды</b>✨\n\n"

                    + "<b>Начать\\закончить работу с ботом</b>\n"
                    + "%s - начать работу со мной\n"
                    + "%s - приостановить работу со мной\n\n"
                    + "%s - получить помощь в работе со мной\n",
            START.getCommandName(), STOP.getCommandName(), HELP.getCommandName());
    public static final String NO_MESSAGE = "Я поддерживаю команды, начинающиеся со слеша(/).\n"
            + "Чтобы посмотреть список команд введите /help";
    public final static String START_MESSAGE = "Привет. Я Javarush Telegram Bot. Я помогу тебе быть в курсе последних " +
            "статей тех авторов, котрые тебе интересны. Я еще маленький и только учусь.";
    public final static String INCOME_MESSAGE = "Введите сумму и категорию в формате \"100.00 категория\"";
    public static final String STOP_MESSAGE = "Деактивировал все ваши подписки \uD83D\uDE1F.";
    public static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F, напишите /help чтобы узнать что я понимаю.";
    private final Map<String, String> commandMap;

    public CommandContainer() {
        commandMap = new HashMap<>();
        commandMap.put(START.getCommandName(),START_MESSAGE);
        commandMap.put(STOP.getCommandName(), STOP_MESSAGE);
        commandMap.put(HELP.getCommandName(), HELP_MESSAGE);
        commandMap.put(NO.getCommandName(), NO_MESSAGE);
        commandMap.put(INCOME.getCommandName(), INCOME_MESSAGE);

    }

    public String retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, UNKNOWN_MESSAGE);
    }

}
