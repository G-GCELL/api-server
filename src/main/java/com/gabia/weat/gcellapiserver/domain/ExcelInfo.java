package com.gabia.weat.gcellapiserver.domain;

import org.hibernate.annotations.SQLDelete;

import com.gabia.weat.gcellapiserver.domain.type.ExcelStatusType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
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
