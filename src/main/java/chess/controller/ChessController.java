package chess.controller;

import java.util.Arrays;
import java.util.List;

import chess.domain.Board;
import chess.domain.Position;
import chess.domain.State;
import chess.view.InputView;
import chess.view.OutputView;

public class ChessController {
	public static final String MOVE = "move";
	public static final String DELIMITER = " ";
	public static final String STATUS = "status";
	public static final String END = "end";
	public static final int COMMAND_ARGUMENTS_SIZE = 3;
	private State state = State.BEFORESTART;

	public void run() {
		OutputView.printGameStartInstruction();
		Board board = new Board();
		setInitialStatus();

		while (state == State.RUNNING && board.isBothKingAlive()) {
			OutputView.printChessBoard(board);
			processSingleTurn(board);
		}
		if (!board.isBothKingAlive()) {
			OutputView.printWinner(board.getWinner());
		}
	}

	private void setInitialStatus() {
		try {
			state = State.of(InputView.inputGameStartOrEnd());
		} catch (Exception e) {
			OutputView.printErrorMessage(e.getMessage());
			setInitialStatus();
		}
	}

	private void processSingleTurn(Board board) {
		try {
			String decision = InputView.inputInstruction();
			processCommand(board, decision);
		} catch (Exception e) {
			OutputView.printErrorMessage(e.getMessage());
		}
	}

	private void processCommand(Board board, String decision) {
		if (MOVE.equals(decision.split(DELIMITER)[0])) {
			List<String> multiArguments = Arrays.asList(decision.split(DELIMITER));
			validateMultiArguments(multiArguments);
			Position source = new Position(multiArguments.get(1));
			Position destination = new Position(multiArguments.get(2));
			board.movePiece(source, destination);
			return;
		}
		if (STATUS.equals(decision)) {
			OutputView.printScore(board);
			OutputView.printTeamWithHigherScore(board);
			return;
		}
		if (END.equals(decision)) {
			state = State.FINISHED;
			return;
		}
		throw new IllegalArgumentException("올바른 명령을 입력해 주십시오.");
	}

	private void validateMultiArguments(List<String> multiArguments) {
		if (multiArguments.size() != COMMAND_ARGUMENTS_SIZE) {
			throw new IllegalArgumentException("올바른 좌표값을 입력해 주세요.");
		}
	}
}
