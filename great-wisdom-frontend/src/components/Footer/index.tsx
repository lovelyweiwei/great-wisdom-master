import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';
const Footer: React.FC = () => {
  const defaultMessage = 'zw出品';
  const currentYear = new Date().getFullYear();
  const beian = '湘ICP备2023016227号-2';
  const beianUrl = 'https://beian.miit.gov.cn/#/Integrated/index';
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={
        <>
          {currentYear} {defaultMessage} | {' '}
          <a href={beianUrl} target="_blank" rel="noreferrer">
            {beian}
          </a>
        </>
      }
      links={[
        {
          key: '大聪明智能 BI',
          title: '大聪明智能 BI',
          href: 'https://gitee.com/weiweibiubiu/great-wisdom-master',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/lovelyweiwei/great-wisdom-master',
          blankTarget: true,
        },
        {
          key: '大聪明智能 BI ',
          title: '大聪明智能 BI ',
          href: 'https://github.com/lovelyweiwei/great-wisdom-master',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
