import { Injectable, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

const TOKEN_KEY = 'token';
const USERNAME_KEY = 'username';
const AUTHORITIES_KEY = 'authorities';
const EXPIRE_KEY = 'date';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private roles: Array<string> = [];
  eventEmitter = new EventEmitter(true);

  constructor(private router: Router) { }

  //cuvanje tokena
  public saveToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
    this.eventEmitter.emit();
  }

  //vraca token ako on postoji i ako nije istekao
  public getToken(): string {
    if (window.sessionStorage.getItem(TOKEN_KEY) != null) {
      const date: Date = new Date(sessionStorage.getItem(EXPIRE_KEY));
      const today: Date = new Date();
      if (date > today) {
        return sessionStorage.getItem(TOKEN_KEY);
      } else {
        this.signOut();
      }
    }
    return null;
  }

  public saveDate(date: Date) {
    window.sessionStorage.removeItem(EXPIRE_KEY);
    window.sessionStorage.setItem(EXPIRE_KEY, date.toString());
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public saveAuthorities(authorities: string[]) {
    window.sessionStorage.removeItem(AUTHORITIES_KEY);
    window.sessionStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
  }

  public getAuthorities(): string[] {
    this.roles = [];
    if (sessionStorage.getItem(TOKEN_KEY)) {
      JSON.parse(sessionStorage.getItem(AUTHORITIES_KEY)).forEach(authority => {
        this.roles.push(authority.authority);
      });
    }
    return this.roles;
  }

  public getAuthority(): string {
    if (this.getToken()) {
      const roles = this.getAuthorities();
      return roles.length === 0 ? null : roles[0];
    }

    return null;
  }

  //odjava
  public signOut() {
    window.sessionStorage.clear();
    this.eventEmitter.emit();
  }
  
}
