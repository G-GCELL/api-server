package com.gabia.weat.gcellapiserver.domain;

import com.gabia.weat.gcellapiserver.domain.type.ExcelStatusType;
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
@SQLDelete(sql = "update excel_info SET status = 'DELETED' WHERE excel_info_id = ?")
@Table(
	indexes = {
		@Index(name = "member_name_status_index", columnList = "member_id, name, status")
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
	@Enumerated(EnumType.STRING)
	private ExcelStatusType status;

	@PrePersist
	public void prePersist(){
		this.status = this.status == null ? ExcelStatusType.CREATING : this.status;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void created() {
		this.status = ExcelStatusType.CREATED;
	}

	@PreRemove
	public void deleteExcelInfo() {
		this.status = ExcelStatusType.DELETED;
	}

}