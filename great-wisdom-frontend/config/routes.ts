export default [
  {
    name: '用户',
    path: '/user',
    layout: false,
    routes: [
      { path: '/user/login', component: './User/Login' },
      { path: '/user/register', name: '注册', component: './User/Register' },
    ],
  },
  { path: '/', redirect: '/add_chart' },
  { path: '/add_chart', name: '智能分析(同步）', icon: 'barChart', component: './AddChart' },
  {
    path: '/add_chart_async',
    name: '智能分析(异步）',
    icon: 'barChart',
    component: './AddChartAsync',
  },
  { path: '/my_chart', name: '我的图表', icon: 'pieChart', component: './MyChart' },
  {
    path: '/my_chart_details/:id',
    name: '我的图表',
    icon: 'pieChart',
    component: './ChartDetails',
    hideInMenu: true,
  },

  {
    path: '/admin',
    name: '管理页面',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', name: '管理页面1', redirect: '/admin/sub-page' },
      { path: '/admin/sub-page', name: '管理页面2', component: './Admin' },
    ],
  },
  { path: '*', layout: false, component: './404' },
];
