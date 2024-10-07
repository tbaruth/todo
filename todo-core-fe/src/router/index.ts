import { createRouter, createWebHistory } from 'vue-router'
import MyTodoLists from "@/todo-lists/views/MyTodoLists.vue";
import {useUserStore} from "@/stores/user";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: MyTodoLists
    }
    //TODO tbaruth feature/2 add more views for adding todo items, etc.
    /*,
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }*/
  ]
});

export default router
