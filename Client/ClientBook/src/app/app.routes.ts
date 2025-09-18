import { CartPageComponent } from './cart-page/cart-page.component';
import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { SignUpPageComponent } from './sign-up-page/sign-up-page.component';
import { UserPagesComponent } from './user-pages/user-pages.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { AddressComponent } from './address/address.component';
import { CheckoutPageComponent } from './checkout-page/checkout-page.component';
import { BookDetailComponent } from './book-detail/book-detail.component';
import { ProductListPageComponent } from './product-list-pages/product-list-pages.component';
import { DashboardComponent } from './dashboard-features/dashboard/dashboard.component';
import { DashBoardProductsComponent } from './dashboard-features/products/products.component';
import { DashBoardOrdersComponent } from './dashboard-features/orders/orders.component';
import { DashBoardCustomersComponent } from './dashboard-features/customers/customers.component';
import { ReportsComponent } from './dashboard-features/reports/reports.component';
import { DashBoardPromotionsComponent } from './dashboard-features/promotions/promotions.component';
import { DashBoardSettingsComponent } from './dashboard-features/settings/settings.component';
import { SearchPageComponent } from './search-page/search-page.component';
import { ProductEditComponent } from './dashboard-features/products/product-edit/product-edit.component';
import { ProductAddComponent } from './dashboard-features/products/product-add/product-add.component';
import { ImportExcelComponent } from './dashboard-features/products/import-excel/import-excel.component';
import { OrdersDetailComponent } from './dashboard-features/orders/orders-detail/orders-detail.component';
import { PromotionEditComponent } from './dashboard-features/promotions/promotion-edit/promotion-edit.component';
import { PromotionCreateComponent } from './dashboard-features/promotions/promotion-create/promotion-create.component';
import { UserResetPasswordComponent } from './dashboard-features/customers/user-reset-password/user-reset-password.component';
import { UserDetailComponent } from './dashboard-features/customers/user-detail/user-detail.component';
import { AuthGuard } from './services/auth.guard';
import { OauthSuccessComponent } from './oauth2-success/oauth2-success.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginPageComponent },

  { path: 'oauth-success', component: OauthSuccessComponent },

  { path: 'signup', component: SignUpPageComponent },
  { path: 'cart', component: CartPageComponent },
  { path: 'account', component: UserPagesComponent },
  { path: 'account/purchase', component: PurchaseComponent },
  { path: 'account/change-pass', component: ChangePasswordComponent },
  { path: 'account/address', component: AddressComponent },
  { path: 'account/checkout', component: CheckoutPageComponent },
  { path: 'books/:id', component: BookDetailComponent },
  { path: 'products', component: ProductListPageComponent },
  { path: 'products/category/:category', component: ProductListPageComponent },
  { path: 'products/latest', component: ProductListPageComponent },
  { path: 'products/best-selling', component: ProductListPageComponent },
  { path: 'products/search-product', component: SearchPageComponent },

  {
    path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard],
    children: [
      {
        path: 'products',
        component: DashBoardProductsComponent,
        children: [
          { path: 'add', component: ProductAddComponent },
          { path: ':id/edit', component: ProductEditComponent },
          { path: 'import-excel', component: ImportExcelComponent }, // 👈 mới
        ],
      },
      {
        path: 'orders',
        component: DashBoardOrdersComponent,
      },
      {
        path: 'orders/:id/view',
        component: OrdersDetailComponent,
      },
      {
        path: 'promotions',
        component: DashBoardPromotionsComponent,
      },
      {
        path: 'promotions/:id/edit',
        component: PromotionEditComponent,
      },
      {
        path: 'promotions/new',
        component: PromotionCreateComponent,
      },
      { path: 'users', component: DashBoardCustomersComponent },
      {
        path: 'users/resetPass/:username',
        component: UserResetPasswordComponent,
      },

      { path: 'users/detail/:username', component: UserDetailComponent },

      { path: 'reports', component: ReportsComponent },
     
      { path: '', redirectTo: 'products', pathMatch: 'full' },
    ],
  },
];
