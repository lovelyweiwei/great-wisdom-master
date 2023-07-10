import Footer from '@/components/Footer';
import { UploadOutlined } from '@ant-design/icons';

import {
  Button,
  Card,
  Col,
  Divider,
  Form,
  Input,
  message,
  Row,
  Select,
  Space,
  Spin,
  Upload,
} from 'antd';
import React, { useState } from 'react';

import TextArea from 'antd/es/input/TextArea';
import {genChartByAiUsingPOST} from '@/services/great-wisdom/chartController';
import ReactECharts from 'echarts-for-react';
import { sub } from 'echarts/types/dist/echarts';
import { Simulate } from 'react-dom/test-utils';
import error = Simulate.error;

/**
 * 添加图表（同步）页面
 * @constructor
 */
const AddChart: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  const [submitting, setSubmitting] = useState<boolean>(false);

  const onFinish = async (values: any) => {
    //避免重复提交
    if (submitting) {
      return;
    }
    setSubmitting(true);
    setChart(undefined);
    setOption(undefined);
    const params = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genChartByAiUsingPOST(params, {}, values.file.file.originFileObj);
      if (!res.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        const chartOption = JSON.parse(res.data.genChart ?? '');
        chartOption.title=undefined;
        if (!chartOption) {
          throw new Error('图表代码解析错误');
        } else {
          setChart(res.data);
          setOption(chartOption);
        }
      }
    } catch (e: any) {
      message.error('分析失败，' + e.message);
    }
    setSubmitting(false);
  };

  return (
    <div className="add_chart">
      <Row gutter={24}>
        <Col span={12}>
          <Card title="智能分析">
            <Form
              name="addChart"
              labelAlign="left"
              labelCol={{ span: 4 }}
              wrapperCol={{ span: 16 }}
              onFinish={onFinish}
              initialValues={{}}
            >
              <Form.Item
                name="goal"
                label="分析目标"
                rules={[{ required: true, message: '请输入您的分析目标!' }]}
              >
                <TextArea placeholder="请输入您的分析需求，比如：分析网站用户的增长情况" />
              </Form.Item>
              <Form.Item name="chartName" label="图表名称">
                <Input placeholder="请输入图表名称" />
              </Form.Item>
              <Form.Item name="chartType" label="图表类型">
                <Select
                  options={[
                    { value: '折线图', label: '折线图' },
                    { value: '柱状图', label: '柱状图' },
                    { value: '漏斗图', label: '漏斗图' },
                    { value: '饼图', label: '饼图' },
                    { value: '堆叠图', label: '堆叠图' },
                    { value: '雷达图', label: '雷达图' },
                    { value: '散点图', label: '散点图' },
                  ]}
                />
              </Form.Item>

              <Form.Item name="file" label="原始数据">
                <Upload name="file" maxCount={1}>
                  <Button icon={<UploadOutlined />}>上传 CSV 文件</Button>
                </Upload>
              </Form.Item>

              <Form.Item wrapperCol={{ span: 16, offset: 4 }}>
                <Space>
                  <Button
                    type="primary"
                    htmlType="submit"
                    loading={submitting}
                    disabled={submitting}
                  >
                    提交
                  </Button>
                  <Button htmlType="reset">重置</Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>
        <Col span={12}>
          <Card title="分析结论">
            {chart?.genResult ?? (
              <div
                style={{
                  color: 'red',
                  textAlign: 'center',
                  fontWeight: 'bold',
                  fontSize: '16px',
                }}
              >
                请先在左侧提交数据
              </div>
            )}
            <Spin spinning={submitting} size="large" />
          </Card>
          {/*<Divider />*/}
          <Divider />
          <Card title="可视化图表">
            {/* {} 不可少，作用重大 */}
            {option ? (
              <ReactECharts option={option} />
            ) : (
              <div
                style={{
                  color: 'red',
                  textAlign: 'center',
                  fontWeight: 'bold',
                  fontSize: '16px',
                }}
              >
                无可视化图表，无法得出结论！
              </div>
            )}
            <Spin spinning={submitting} size="large" />
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
