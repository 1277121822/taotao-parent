package com.djb.taotao.manager.service.impl;

import com.djb.taotao.common.pojo.EasyUIDataGridResult;
import com.djb.taotao.common.utils.IDUtils;
import com.djb.taotao.common.utils.JsonUtils;
import com.djb.taotao.common.utils.TaotaoResult;
import com.djb.taotao.manager.jedis.JedisClient;
import com.djb.taotao.mapper.TbItemDescMapper;
import com.djb.taotao.mapper.TbItemMapper;
import com.djb.taotao.pojo.TbItem;
import com.djb.taotao.pojo.TbItemDesc;
import com.djb.taotao.pojo.TbItemDescExample;
import com.djb.taotao.pojo.TbItemExample;
import com.djb.taotao.manager.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper mapper;
    @Autowired
    private TbItemDescMapper descmapper;
    @Autowired
    private JedisClient client;

    @Autowired
    private JmsTemplate jmstemplate;

    @Resource(name = "topicDestination")
    private Destination destination;

    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY;

    @Value("${ITEM_INFO_KEY_EXPIRE}")
    private Integer ITEM_INFO_KEY_EXPIRE;

    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        // 1.设置分页的信息 使用pagehelper
        if (page == null)
            page = 1;
        if (rows == null)
            rows = 30;
        PageHelper.startPage(page, rows);
        // 2.注入mapper
        // 3.创建example 对象 不需要设置查询条件
        TbItemExample example = new TbItemExample();
        // 4.根据mapper调用查询所有数据的方法
        List<TbItem> list = mapper.selectByExample(example);
        // 5.获取分页的信息
        PageInfo<TbItem> info = new PageInfo<>(list);
        // 6.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        // 7.返回
        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        // 生成商品的id
        final long itemId = IDUtils.genItemId();
        // 1.补全item 的其他属性
        item.setId(itemId);
        item.setCreated(new Date());
        // 1-正常，2-下架，3-删除',
        item.setStatus((byte) 1);
        item.setUpdated(item.getCreated());
        // 2.插入到item表 商品的基本信息表
        mapper.insertSelective(item);
        // 3.补全商品描述中的属性
        TbItemDesc desc2 = new TbItemDesc();
        desc2.setItemDesc(desc);
        desc2.setItemId(itemId);
        desc2.setCreated(item.getCreated());
        desc2.setUpdated(item.getCreated());
        // 4.插入商品描述数据
        // 注入tbitemdesc的mapper
        descmapper.insertSelective(desc2);

        // 添加发送消息的业务逻辑
        jmstemplate.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                // 发送的消息
                return session.createTextMessage(itemId + "");
            }
        });
        // 5.返回taotaoresult
        return TaotaoResult.ok();
    }

    @Override
    public TbItem getItemById(Long itemId) {

        // 添加缓存

        // 1.从缓存中获取数据，如果有直接返回
        try {
            String jsonstr = client.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");

            if (StringUtils.isNotBlank(jsonstr)) {
                // 重新设置商品的有效期
                client.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
                return JsonUtils.jsonToPojo(jsonstr, TbItem.class);

            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // 2如果没有数据

        // 注入mapper
        // 调用方法TbItemDescExample example = new TbItemDescExample();
        //        example.createCriteria().andItemIdEqualTo(itemId);
        //        List<TbItemDesc> tbItemDescs = descmapper.selectByExample(example);
        System.out.println("看下面");
        TbItemExample example = new TbItemExample();
        example.createCriteria().andIdEqualTo(itemId);
        List<TbItem> list = mapper.selectByExample(example);
        TbItem tbItem = list.get(0);
        System.out.println(JsonUtils.objectToJson(tbItem));
        // 返回tbitem

        // 3.添加缓存到redis数据库中
        // 注入jedisclient
        // ITEM_INFO:123456:BASE
        // ITEM_INFO:123456:DESC
        try {
            client.set(ITEM_INFO_KEY + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
            // 设置缓存的有效期
            client.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_KEY_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }



    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        // 添加缓存

        // 1.从缓存中获取数据，如果有直接返回
        try {
            String jsonstr = client.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");

            if (StringUtils.isNotBlank(jsonstr)) {
                // 重新设置商品的有效期
                System.out.println("有缓存");
                client.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
                return JsonUtils.jsonToPojo(jsonstr, TbItemDesc.class);

            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //如果没有查到数据 从数据库中查询
        System.out.println("看下面");
        TbItemDescExample example = new TbItemDescExample();
        example.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemDesc> tbItemDescs = descmapper.selectByExample(example);
        //添加缓存
        // 3.添加缓存到redis数据库中
        // 注入jedisclient
        // ITEM_INFO:123456:BASE
        // ITEM_INFO:123456:DESC
        TbItemDesc itemdesc = tbItemDescs.get(0);
        System.out.println(JsonUtils.objectToJson(itemdesc));
        try {
            client.set(ITEM_INFO_KEY + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemdesc));
            // 设置缓存的有效期
            client.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_KEY_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemdesc;
    }

}
