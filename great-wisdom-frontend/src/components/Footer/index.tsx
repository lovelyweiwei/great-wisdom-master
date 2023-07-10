import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';
const Footer: React.FC = () => {
  const defaultMessage = 'weiwei';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
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
