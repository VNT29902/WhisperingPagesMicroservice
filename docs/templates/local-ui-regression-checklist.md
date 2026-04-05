# Local UI Regression Checklist (Post-change)

## Build info
- Branch:
- Commit SHA:
- Tester:
- Date:

## 1) Environment bootstrap
- [ ] Docker stack up successfully
- [ ] Frontend accessible
- [ ] Gateway accessible
- [ ] Required test account/data available

## 2) Core customer flows
- [ ] Register/login/logout
- [ ] Browse by category
- [ ] Search and open product detail
- [ ] Add/update/remove cart items
- [ ] Checkout and create order
- [ ] View purchase history

## 3) Admin flows
- [ ] Product CRUD
- [ ] Promotion management
- [ ] Customer management
- [ ] Order management
- [ ] Dashboard reports load

## 4) UI quality checks
- [ ] Loading state
- [ ] Empty state
- [ ] Validation error state
- [ ] API failure fallback state
- [ ] No major console errors

## 5) Responsive and browser checks
- [ ] Desktop layout
- [ ] Mobile layout
- [ ] Main supported browser verified

## 6) Evidence
- [ ] Screenshots/video attached
- [ ] Bug tickets created for issues found
- [ ] Checklist attached to PR
