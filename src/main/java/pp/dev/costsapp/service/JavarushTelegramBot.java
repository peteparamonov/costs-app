package pp.dev.costsapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pp.dev.costsapp.model.UserSession;
import pp.dev.costsapp.model.dictionary.IncomeCategory;
import pp.dev.costsapp.model.dictionary.OperationType;
import pp.dev.costsapp.model.dto.OperationSaveDto;
import pp.dev.costsapp.model.entity.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JavarushTelegramBot extends TelegramLongPollingBot {
    private Map<String, UserSession> userSessions = new HashMap<>();

    public static String COMMAND_PREFIX = "/";
    private static final String DECIMAL_OPERATION_REGEX = "^\\d{1,10}\\.\\d{0,2}\\s[а-яА-Я]{1,100}";
    private static final String INT_OPERATION_REGEX = "^\\d{1,10}\\s[а-яА-Я]{1,100}";
    private static final String SET_AMOUNT_MESSAGE = "Введите сумму в формате \"100.00 комментарий\". Число может быть без дробной части и без комментария";
    private static final String OPERATION_BUTTON = "OPERATION_BUTTON";
    private static final String WALLET_BUTTON = "WALLET_BUTTON";
    private static final String BALANCE_BUTTON = "BALANCE_BUTTON";
    private static final String ADD_BUTTON = "ADD_BUTTON";
    private static final String DELETE_BUTTON = "DELETE_BUTTON";
    private static final String VIEW_BUTTON = "VIEW_BUTTON";
    private static final String INCOME_BUTTON = "INCOME_BUTTON";
    private static final String SPENDING_BUTTON = "SPENDING_BUTTON";
    private static final String CHOOSE_WALLET_BUTTON = "CHOOSE_WALLET_BUTTON";
    static final String ERROR_TEXT = "Error occurred: ";

    @Value("${bot.name}")
    private String username;

    private OperationService operationService;
    private WalletService walletService;

    @Autowired
    public JavarushTelegramBot(@Value("${bot.token}") String token, OperationService operationService, WalletService walletService) {
        super(token);
        this.operationService = operationService;
        this.walletService = walletService;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "see available choices"));
        listofCommands.add(new BotCommand("/delete", "delete all data"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String chatId = String.valueOf(update.getMessage().getChatId());
            switch (message) {
                case "/start" -> startCommand(chatId);
                case "/delete" -> deleteData(chatId);
                default -> prepareAndSendMessage(chatId, "Sorry, command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {

            String callbackData = update.getCallbackQuery().getData();
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());

            if (callbackData.equals(OPERATION_BUTTON)) {
                operationCommand(chatId);
            }
            if (callbackData.equals(ADD_BUTTON)) {
                addOperationCommand(chatId);
            }
            if (callbackData.equals(INCOME_BUTTON)) {
                UserSession userSession = new UserSession();
                List<String> userAnswers = new ArrayList<>(List.of(OperationType.INCOME.name()));
                userSession.setAnswers(userAnswers);
                userSessions.put(chatId, userSession);
                incomeOperationCommand(chatId);
            }
            if (EnumSet.allOf(IncomeCategory.class).stream().map(Enum::name).toList().contains(callbackData)) {
                UserSession userSession = userSessions.getOrDefault(chatId, new UserSession(Collections.emptyList()));
                userSession.getAnswers(). add(callbackData);
                if (update.hasMessage() && update.getMessage().hasText()) {
                    String message = update.getMessage().getText().trim();
                    if (message.matches(DECIMAL_OPERATION_REGEX) || message.matches(INT_OPERATION_REGEX)) {
                        String[] operationInfo = message.split(" ");
                        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(operationInfo[0]));
                        String nameArray = Arrays.stream(operationInfo).collect(Collectors.toList()).remove(0);
                        String comment = StringUtils.join(nameArray, " ");
                        List<String> answers = userSessions.get(chatId).getAnswers();
                        Operation savedOperation = operationService.save(
                                new OperationSaveDto(OperationType.valueOf(answers.get(0)),
                                        answers.get(1),
                                        amount,
                                        comment,
                                        LocalDate.now()));
                        sendMessage(chatId, "Добавлена операция на сумму " + savedOperation.getAmount());
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    private void startCommand(String chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете нужную категорию");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var operationButton = new InlineKeyboardButton();

        operationButton.setText("Операция");
        operationButton.setCallbackData(OPERATION_BUTTON);

        var walletButton = new InlineKeyboardButton();

        walletButton.setText("Кошелёк");
        walletButton.setCallbackData(WALLET_BUTTON);

        var balanceButton = new InlineKeyboardButton();

        balanceButton.setText("Баланс");
        balanceButton.setCallbackData(BALANCE_BUTTON);

        rowInLine.add(operationButton);
        rowInLine.add(walletButton);
        rowInLine.add(balanceButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }

    private void operationCommand(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете тип операции");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var addButton = new InlineKeyboardButton();

        addButton.setText("Добавить операцию");
        addButton.setCallbackData(ADD_BUTTON);

        var deleteButton = new InlineKeyboardButton();

        deleteButton.setText("Удалить последнюю операцию");
        deleteButton.setCallbackData(DELETE_BUTTON);

        var viewButton = new InlineKeyboardButton();

        viewButton.setText("Просмотреть все операции");
        viewButton.setCallbackData(VIEW_BUTTON);

        rowInLine.add(addButton);
        rowInLine.add(deleteButton);
        rowInLine.add(viewButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }

    private void addOperationCommand(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете тип операции");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var incomeButton = new InlineKeyboardButton();

        incomeButton.setText("Поступление");
        incomeButton.setCallbackData(INCOME_BUTTON);

        var spendingButton = new InlineKeyboardButton();

        spendingButton.setText("Списание");
        spendingButton.setCallbackData(SPENDING_BUTTON);

        rowInLine.add(incomeButton);
        rowInLine.add(spendingButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }

    private void incomeOperationCommand(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберете категорию");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        Arrays.stream(IncomeCategory.values()).forEach(category -> {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();

            var incomeButton = new InlineKeyboardButton();

            incomeButton.setText(category.getCategoryValue());
            incomeButton.setCallbackData(category.name());
            rowInLine.add(incomeButton);
            rowsInLine.add(rowInLine);
        });

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            //todo add logging to the project.
            e.printStackTrace();
        }
    }

    public void deleteData(String chatId) {
        operationService.deleteAllByChatId(chatId);
        walletService.deleteAllByChatId(chatId);

    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void prepareAndSendMessage(String chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        executeMessage(message);
    }
}
