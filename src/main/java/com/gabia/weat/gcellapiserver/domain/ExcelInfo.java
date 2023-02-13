package com.gabia.weat.gcellapiserver.domain;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "excel_info")
@Table(
	indexes = {
		@Index(name = "member_name_is_deleted_index", columnList = "member_id, name, is_deleted")
	}
)
public class ExcelInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long excelInfoId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@Column
	private String name;
	@Column
	private String path;
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	public void updateName(String name) {
		this.name = name;
	}

	public void validate(String memberEmail) {
		if (this.member == null || !this.getMember().getEmail().equals(memberEmail)) {
			throw new CustomException(ErrorCode.EXCEL_NOT_MATCHES);
		}
		if (this.isDeleted) {
			throw new CustomException(ErrorCode.EXCEL_DELETED);
		}
	}

}