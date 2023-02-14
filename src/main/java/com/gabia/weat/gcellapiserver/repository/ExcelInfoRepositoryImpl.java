package com.gabia.weat.gcellapiserver.repository;

import static com.gabia.weat.gcellapiserver.domain.QExcelInfo.*;
import static com.gabia.weat.gcellapiserver.dto.FileDto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.dto.FileDto;
import com.gabia.weat.gcellapiserver.repository.enums.CreatedAtCondition;
import com.gabia.weat.gcellapiserver.repository.enums.IdCondition;
import com.gabia.weat.gcellapiserver.repository.enums.NameCondition;
import com.gabia.weat.gcellapiserver.repository.enums.OrderCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExcelInfoRepositoryImpl implements ExcelInfoRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ExcelInfo> findByMemberPaging(Member member, FileListRequestDto fileListRequestDto
	, Pageable pageable) {
		List<Long> excelInfoIdList = queryFactory.select(excelInfo.excelInfoId)
			.from(excelInfo)
			.where(excelInfo.member.eq(member),
				conditionExcelInfoId(fileListRequestDto.excelInfoId(), fileListRequestDto.idCondition()),
				conditionExcelInfoName(fileListRequestDto.fileName(), fileListRequestDto.nameCondition()),
				conditionCreatedAt(fileListRequestDto.createdAt(), fileListRequestDto.createdAtCondition()),
				conditionIsDelete(fileListRequestDto.isDelete())
			)
			.orderBy(excelInfoSort(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<ExcelInfo> excelInfoList = queryFactory.selectFrom(excelInfo)
			.where(excelInfo.excelInfoId.in(excelInfoIdList))
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(excelInfo.count()).from(excelInfo);

		return PageableExecutionUtils.getPage(excelInfoList, pageable, countQuery::fetchOne);
	}

	private BooleanExpression conditionExcelInfoId(Long excelInfoId, IdCondition idCondition){
		if (excelInfoId == 0L){
			return null;
		}
		if (idCondition.equals(IdCondition.IN)){
			return excelInfo.excelInfoId.in(excelInfoId);
		}
		if (idCondition.equals(IdCondition.NOT_IN)){
			return excelInfo.excelInfoId.notIn(excelInfoId);
		}
		return excelInfo.excelInfoId.eq(excelInfoId);
	}

	private BooleanExpression conditionExcelInfoName(String excelInfoName, NameCondition nameCondition){
		if (StringUtils.isNullOrEmpty(excelInfoName)){
			return null;
		}
		if (nameCondition.equals(NameCondition.LIKE)){
			return excelInfo.name.contains(excelInfoName);
		}
		return excelInfo.name.eq(excelInfoName);
	}

	private BooleanExpression conditionCreatedAt(LocalDateTime createdAt, CreatedAtCondition createdAtCondition){
		if (createdAt == null){
			return null;
		}
		if (createdAtCondition.equals(CreatedAtCondition.LESS_THAN)){
			return excelInfo.createdAt.before(createdAt);
		}
		return excelInfo.createdAt.after(createdAt);
	}

	private BooleanExpression conditionIsDelete(Boolean isDelete){
		if (isDelete == null){
			return null;
		}
		return excelInfo.isDeleted.eq(isDelete);
	}

	private OrderSpecifier<?> excelInfoSort(Pageable pageable){
		if (!pageable.getSort().isEmpty()){
			for (Sort.Order order :
				pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				if (order.getProperty().equals("createdAt")){
					return new OrderSpecifier(direction, excelInfo.createdAt);
				}
				return new OrderSpecifier(direction, excelInfo.name);
			}
		}
		return null;
	}


}
