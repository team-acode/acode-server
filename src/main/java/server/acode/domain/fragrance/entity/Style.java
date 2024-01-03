package server.acode.domain.fragrance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Style extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_id")
    private Long id;

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean chic; //시크한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean mature; //성숙한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean luxurious; //고급스러운

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean elegant; //우아한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean masculine; //남성적인

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean comfortable; //편안한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean serene; //차분한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean light; //가벼운

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean neutral; //중성적인

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean friendly; //친근한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean clean; //깨끗한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean sensual; //관능적인

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean delicate; //은은한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean lively; //활기찬

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean lovely; //사랑스러운

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean bright; //밝은

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean radiant; //화사한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean feminine; //여성스러운

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean innocent; //청순한

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean weighty; //무게감 있는

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean soft; //부드러운

    @Column(columnDefinition="tinyint(0) default 0")
    private boolean cozy; //포근한

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;
}
