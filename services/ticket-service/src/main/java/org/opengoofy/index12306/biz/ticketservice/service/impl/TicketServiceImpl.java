package org.opengoofy.index12306.biz.ticketservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opengoofy.index12306.biz.ticketservice.dao.entity.TrainDO;
import org.opengoofy.index12306.biz.ticketservice.dao.entity.TrainStationPriceDO;
import org.opengoofy.index12306.biz.ticketservice.dao.entity.TrainStationRelationDO;
import org.opengoofy.index12306.biz.ticketservice.dao.mapper.TrainMapper;
import org.opengoofy.index12306.biz.ticketservice.dao.mapper.TrainStationPriceMapper;
import org.opengoofy.index12306.biz.ticketservice.dao.mapper.TrainStationRelationMapper;
import org.opengoofy.index12306.biz.ticketservice.dto.domain.BulletTrainDTO;
import org.opengoofy.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.opengoofy.index12306.biz.ticketservice.dto.resp.TicketPageQueryRespDTO;
import org.opengoofy.index12306.biz.ticketservice.service.TicketService;
import org.opengoofy.index12306.biz.ticketservice.toolkit.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;
    private final TrainMapper trainMapper;

    @Override
    public IPage<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO requestParam) {
        LambdaQueryWrapper<TrainStationRelationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationRelationDO.class)
                .eq(TrainStationRelationDO::getStartRegion, requestParam.getFromStation())
                .eq(TrainStationRelationDO::getEndRegion, requestParam.getToStation());
        IPage<TrainStationRelationDO> trainStationRelationPage = trainStationRelationMapper.selectPage(requestParam,
                queryWrapper);
        return trainStationRelationPage.convert(each -> {
            LambdaQueryWrapper<TrainDO> trainQueryWrapper = Wrappers.lambdaQuery(TrainDO.class)
                    .eq(TrainDO::getId, each.getTrainId());
            TrainDO trainDO = trainMapper.selectOne(trainQueryWrapper);
            TicketPageQueryRespDTO ticketPageQueryRespDTO = new TicketPageQueryRespDTO();
            ticketPageQueryRespDTO.setArrival(each.getArrival());
            ticketPageQueryRespDTO.setDeparture(each.getDeparture());
            ticketPageQueryRespDTO.setDuration(DateUtil.calculateHourDifference(each.getArrivalTime(),
                    each.getDepartureTime()));
            ticketPageQueryRespDTO.setTrainNumber(trainDO.getTrainNumber());
            ticketPageQueryRespDTO.setArrivalTime(each.getArrivalTime());
            ticketPageQueryRespDTO.setDepartureTime(each.getDepartureTime());
            //TODO 后续改为三种类型，目前0为高铁，1为普通火车
            if (Objects.equals(trainDO.getTrainType(), 0)) {
                BulletTrainDTO bulletTrainDTO = new BulletTrainDTO();
                LambdaQueryWrapper<TrainStationPriceDO> seatTypeQueryWrapper =
                        Wrappers.lambdaQuery(TrainStationPriceDO.class)
                        .eq(TrainStationPriceDO::getArrival, requestParam.getArrival())
                        .eq(TrainStationPriceDO::getDeparture, requestParam.getDeparture())
                        .eq(TrainStationPriceDO::getTrainId, trainDO.getId());
                List<TrainStationPriceDO> stationPriceDOList =
                        trainStationPriceMapper.selectList(seatTypeQueryWrapper);
                stationPriceDOList.forEach(item -> {
                    switch (item.getSeatType()) {
                        case 0:
                            bulletTrainDTO.setBusinessClassPrice(item.getPrice());
                        case 1:
                            bulletTrainDTO.setFirstClassPrice(item.getPrice());
                        case 2:
                            bulletTrainDTO.setSecondClassPrice(item.getPrice());
                    }
                    ticketPageQueryRespDTO.setBulletTrain(bulletTrainDTO);
                });
            }
            return ticketPageQueryRespDTO;
        });
    }
}
