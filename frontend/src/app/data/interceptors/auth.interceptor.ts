import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { Router } from "@angular/router";

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {

    const authService = inject(AuthService);

    const router = inject(Router);

    const [login, password, role] = authService.getUserInfo();

    if (login && password && role && router.url != "/login") {
        req = req.clone({
            setHeaders: {
                Authorization: `Basic ${btoa(`${login}:${password}`)}`
            }
        });
    }

    return next(req);
}