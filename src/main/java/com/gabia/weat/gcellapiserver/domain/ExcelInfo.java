package com.gabia.weat.gcellapiserver.domain;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "excel_info")
@SQLDelete(sql = "update excel_info SET is_deleted = true WHERE excel_info_id = ?")
@Table(
	uniqueConstraints = {
		@UniqueConstraint(name = "member_name_unique", columnNames = {"member_id", "name"})
	}
)
public class ExcelInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long excelInfoId;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "member_id")
	private Member member;
	@Column
	private String name;
	@Column
	private String path;
	@Column
	private Boolean isDeleted;

	public void updateName(String name) {
		this.name = name;
	}

	@PreRemove
	public void deleteExcelInfo(){
		this.isDeleted = true;
	}


}