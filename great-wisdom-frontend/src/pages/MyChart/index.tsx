import React, { useEffect, useState } from 'react';
import { listMychartByPageUsingPOST } from '@/services/great-wisdom/chartController';
import { Avatar, Button, Card, List, message, Result } from 'antd';
import ReactECharts from 'echarts-for-react';
import { useModel } from '@@/exports';
import { char } from 'stylis';
import Search from 'antd/es/input/Search';

/**
 * 我的图表页面
 * @constructor
 */
const MyChartPage: React.FC = () => {
  const initSearchParams = {
    current: 1,
    pageSize: 12,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({ ...initSearchParams });
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  const [chartList, setChartList] = useState<API.Chart[]>();
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listMychartByPageUsingPOST(searchParams);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
        // 隐藏图表的title
        if (res.data.records) {
          res.data.records.forEach((data) => {
            if (data.chartStatus === 'succeed') {
              const chartOption = JSON.parse(data.genChart ?? '{}');
              chartOption.title = undefined;
              data.genChart = JSON.stringify(chartOption);
            }
          });
        }
      }
    } catch (e: any) {
      message.error('获取图表失败，' + e.message);
    }
    setLoading(false);
  };

  // 当react首次渲染以及 deps 的数据发生变化时执行内部的函数
  useEffect(() => {
    loadData();
  }, [searchParams]);

  return (
    <div className="my-chart-page">
      <div className="margin-16">
        <Search
          placeholder="请输入图表名称搜索"
          loading={loading}
          enterButton
          onSearch={(value) => {
            setSearchParams({
              ...initSearchParams,
              chartName: value,
            });
          }}
        />
      </div>

      <List
        grid={{
          gutter: 16,
          xs: 1,
          sm: 1,
          md: 1,
          lg: 1,
          xl: 2,
          xxl: 2,
        }}
        pagination={{
          // 设置分页
          showTotal: () => `共 ${total} 条记录`,
          showSizeChanger: true,
          showQuickJumper: true,
          pageSizeOptions: ['6', '10', '14', '20'],
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            });
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: total,
        }}
        loading={loading}
        dataSource={chartList}
        renderItem={(item) => (
          <List.Item key={item.id}>
            <Card style={{ width: '100%' }}>
              <List.Item.Meta
                avatar={<Avatar src={currentUser?.userAvatar} />}
                title={item.chartName}
                description={item.chartType ? '图表类型：' + item.chartType : undefined}
              />
              <>
                {item.chartStatus === 'wait' && (
                  <>
                    <Result
                      status="warning"
                      title="待生成"
                      subTitle={item.execMessage ?? '当前；图表生成繁忙，请耐心等待'}
                    />
                  </>
                )}

                {item.chartStatus === 'running' && (
                  <>
                    <Result status="info" title="图表生成中" subTitle={item.execMessage} />
                  </>
                )}

                {item.chartStatus === 'succeed' && (
                  <>
                    <div style={{ marginBottom: 8 }} />
                    <p
                      style={{
                        // textAlign: 'center',
                        fontWeight: 'bold',
                        color: 'black',
                        fontSize: '16px',
                      }}
                    >
                      {'分析目标：' + item.goal}
                    </p>
                    <div style={{ marginBottom: 16 }} />
                    <ReactECharts option={JSON.parse(item.genChart ?? '{}')} />
                  </>
                )}

                {item.chartStatus === 'failed' && (
                  <>
                    <Result status="error" title="图表生成失败" subTitle={item.execMessage} />
                  </>
                )}
              </>
            </Card>
          </List.Item>
        )}
      />
    </div>
  );
};
export default MyChartPage;
