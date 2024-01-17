import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AchievementDetailsDTO } from '../../models/AchievementDetailsDTO';

@Injectable({
  providedIn: 'root'
})
export class AchievementService {

  constructor(
    private http: HttpClient
  ) {}

  getAchievementById(id: number): Observable<AchievementDetailsDTO> {
    return this.http.get<AchievementDetailsDTO>(`/api/v1/achievements/${id}`);
  }

  getAchievementImage(imageId: number): Observable<Blob> {
    return this.http.get(`api/v1/images/${imageId}`, { responseType: 'blob' });
  }
}
