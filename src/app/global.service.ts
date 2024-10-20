import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpRequest} from "@angular/common/http";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  constructor(private router:Router, private http:HttpClient) { }

  handleHttpError(error: HttpErrorResponse, request: HttpRequest<any>): string {


    if (error.url && error.url.includes('https://api.ipify.org/')) {

      return `Request to ${error.url} failed: ${error.message}`;
    }

    switch (error.status) {
      case 200: {
        return `Successfully`;
      }
      case 201: {
        return `Successfully`;
      }
      case 400: {
        // this.showMsg(
        //   'Error → ' +
        //   (error.error && error.error.text
        //     ? error.error.text
        //     : error.error ||
        //     'Please check fields,\n Or contact development team'),
        //   'Something Went Wrong',
        //   'error_snack'
        // );
        return `Please check your request: ${error.message}`;
      }
      case 405: {
        // this.showMsg(
        //   'Method Not Allowed → ' +
        //   (error.error && error.error.text
        //     ? error.error.text
        //     : error.error ||
        //     'Please check fields,\n Or contact development team'),
        //   'Something Went Wrong',
        //   'error_snack'
        // );
        return `Please check your request: ${error.message}`;
      }
      case 406: {
        // this.showMsg(
        //   'Not Acceptable → ' +
        //   (error.error && error.error.text
        //     ? error.error.text
        //     : error.error ||
        //     'Please check fields,\n Or contact development team'),
        //   'Something Went Wrong',
        //   'error_snack'
        // );
        return `Please check your request: ${error.message}`;
      }
      case 401: {
        const currentRoute = this.router.url; // Get the current route URL

        // Check if the current route is the OtpComponent route
        if (!currentRoute.startsWith('/otp/')) {
          this.router.navigate(['sign-in']);
          // this.showMsg(
          //   'Unauthorized, You Have to reconnect',
          //   'Something Went Wrong',
          //   'error_snack'
          // );
        }

        return `Unauthorized: ${error.message}`;
      }
      case 403: {
        const currentRoute = this.router.url;
        if (!currentRoute.startsWith('/otp/')) {
          this.router.navigate(['sign-in']);
          // this.showMsg(
          //   'Forbidden , You Have to reconnect',
          //   'Something Went Wrong',
          //   'error_snack'
          // );
        }
        return `Access Denied: ${error.message}`;
      }
      case 404: {
        const currentRoute = this.router.url;
        if (error.url!.includes('/api/v1/chat/messages/')) {
          return 'room not created yet';
        } else if (error.url!.includes('/api/v1/logout')) {
          // this.showMsgInfo('Disconnected');
          return 'Disconnected';
        } else if (error.url!.includes('/api/v1/auth')) {
          return '';
        } else {
          this.router.navigate(['404']);
          return `Access Denied: ${error.message}`;
        }
      }
      case 500: {
        const currentRoute = this.router.url;
        if (!currentRoute.startsWith('/otp/')) {
          if (error.url!.includes('https://server.camel-soft.com/')) {
            this.router.navigate(['500']);
          }
          // this.showMsg('Something Went Wrong', 'Oops!', 'error_snack');
        }
        return `Internal Server Error: ${error.message}`;
      }
      case 502: {
        const currentRoute = this.router.url;
        if (!currentRoute.startsWith('/otp/')) {
          // this.router.navigate(['502']);
          // this.showMsg('Something Went Wrong', 'Oops!', 'error_snack');
        }
        return `Internal Server Error: ${error.message}`;
      }
      case 0: {
        const currentRoute = this.router.url;
        if (!currentRoute.startsWith('/otp/')) {
          // this.router.navigate(['502']);
          // this.showMsg('TimeOut ! Please Try Again', 'Oops!', 'error_snack');
        }

        return `Internal Server Error: ${error.message}`;
      }
      case 504: {
        const currentRoute = this.router.url;
        if (!currentRoute.startsWith('/otp/')) {
          // this.showMsg('TimeOut ! Please Try Again', 'Oops!', 'error_snack');
        }

        return `Internal Server Error: ${error.message}`;
      }
      default: {
        const currentRoute = this.router.url;
        console.log("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeey",this.router.url)
        if (!currentRoute.startsWith('/otp/') && !currentRoute.startsWith('/Supplier-management') && !currentRoute.startsWith('/Manage-Agents')) {
          // this.showMsg(
          //   'Error → ' +
          //   (error.error && error.error.text
          //     ? error.error.text
          //     : error.error ||
          //     'Some error has occured ,\n Or contact development team'),
          //   'Something Went Wrong',
          //   'error_snack'
          // );
        }
        return `Unknown Server Error: ${error.message}`;
      }
    }
  }
}
