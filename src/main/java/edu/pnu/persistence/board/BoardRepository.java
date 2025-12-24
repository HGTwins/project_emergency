package edu.pnu.persistence.board;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
