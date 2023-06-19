import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
    history: createWebHistory(
        import.meta.env.BASE_URL),
    routes: [{
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/search',
            name: 'search',
            component: () =>
                import ('../views/SearchView.vue')
        },
        {
            path: '/create',
            name: 'create',
            component: () =>
                import ('../views/CreateView.vue')
        },
        {
            path: '/edit/:userId',
            name: 'edit',
            component: () =>
                import ('../components/Registration.vue')
        }
    ]
})

export default router