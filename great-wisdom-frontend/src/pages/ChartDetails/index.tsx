import React, {useEffect, useState} from 'react';
import {Badge, Card, Descriptions, message} from 'antd';

import {getchartByIdUsingGET} from '../../services/great-wisdom/chartController';
import {PageContainer} from '@ant-design/pro-components';
import moment from 'moment';
import {useParams} from '../../.umi/exports';
import ReactECharts from 'echarts-for-react';

/**
 * 我的图表页面
 * @constructor
 */
const ChartDetails: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.Chart>();
  const params = useParams();

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await getchartByIdUsingGET({
        // id: Number(params.id),
        id: params.id,
      });
      setData(res.data);
    } catch (error: any) {
      message.error('请求失败' + error.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title={'查看图表详情'}>
      <Card>
        {data ? (
          <Descriptions bordered>
            <Descriptions.Item label="图表名称" span={1}>
              {data.chartName}
            </Descriptions.Item>
            <Descriptions.Item label="分析目标" span={3}>
              {data.goal}
            </Descriptions.Item>
            <Descriptions.Item label="图表类型" span={1}>
              {data.chartType}
            </Descriptions.Item>
            <Descriptions.Item span={1} label="创建时间">
              {moment(data.createTime).format('YYYY-MM-DD HH:mm:ss')}
            </Descriptions.Item>
            <Descriptions.Item label="更新时间" span={1}>
              {moment(data.updateTime).format('YYYY-MM-DD HH:mm:ss')}
            </Descriptions.Item>
            <Descriptions.Item label="生成状态" span={1}>
              {data.chartStatus === 'succeed' && <Badge status="success" text="生成成功"/>}
              {data.chartStatus === 'wait' && <Badge status="warning" text="等待生成"/>}
              {data.chartStatus === 'running' && <Badge status="processing" text="生成中"/>}
              {data.chartStatus === 'failed' && <Badge status="error" text="生成失败"/>}
            </Descriptions.Item>
            <Descriptions.Item label="执行结果" span={3}>
              {data.execMessage}
            </Descriptions.Item>
            <Descriptions.Item label="压缩后CSV" span={3}>
              {data.chartData}
            </Descriptions.Item>
            <Descriptions.Item label="ECharts原始生成数据" span={3}>
              {data.genChart}
            </Descriptions.Item>
            <Descriptions.Item label="分析结论" span={3}>
              {data.genResult}
            </Descriptions.Item>
            <Descriptions.Item label="生成图表" span={3}>
              <ReactECharts option={JSON.parse(data.genChart ?? '{}')}/>
            </Descriptions.Item>
          </Descriptions>
        ) : (
          <>图表不存在</>
        )}
      </Card>
    </PageContainer>
  );
};

export default ChartDetails;
