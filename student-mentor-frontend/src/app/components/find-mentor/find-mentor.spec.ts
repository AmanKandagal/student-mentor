import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindMentor } from './find-mentor';

describe('FindMentor', () => {
  let component: FindMentor;
  let fixture: ComponentFixture<FindMentor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FindMentor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FindMentor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
