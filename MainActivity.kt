package com.example.passatunderground

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.*
import kotlin.math.*

class MainActivity : Activity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  window.decorView.systemUiVisibility = 5894 or 1024 or 512
  setContentView(RaceView(this))
 }
}

private class RaceView(ctx: Context) : View(ctx) {
 private val p = Paint(3)
 private var last = System.nanoTime()
 private var mode = 0
 private var offset = 0f
 private var xcar = 0f
 private var speed = 0f
 private var nitro = 100f
 private var score = 0
 private var cash = 2500
 private var gas = false
 private var brake = false
 private var boost = false
 private var steer = 0f
 private var colorIndex = 0
 private val colors = intArrayOf(Color.LTGRAY, Color.BLUE, Color.RED, Color.BLACK)
 private val traffic = mutableListOf<FloatArray>()
 private val random = java.util.Random()

 override fun onDraw(c: Canvas) {
  val dt = ((System.nanoTime()-last)/1e9).toFloat().coerceIn(0f,.04f); last=System.nanoTime()
  if(mode==1) update(dt)
  scene(c,width.toFloat(),height.toFloat())
  postInvalidateOnAnimation()
 }
 private fun update(dt:Float) {
  speed += ((if(gas) 1f else if(brake) .2f else .55f)-speed)*dt*1.5f
  if(boost && nitro>0) { speed=min(1.3f,speed+dt); nitro=max(0f,nitro-dt*25) } else nitro=min(100f,nitro+dt*4)
  xcar=(xcar+steer*dt*1.2f).coerceIn(-.85f,.85f)
  offset=(offset+dt*(200+speed*400))%1000
  score+=(dt*speed*20).toInt()
  if(random.nextFloat()<dt*.7f) traffic.add(floatArrayOf(random.nextFloat()*1.2f-.6f,-.1f))
  val iter=traffic.iterator()
  while(iter.hasNext()) { val a=iter.next(); a[1]+=dt*(.35f+speed*.5f); if(a[1]>1.1f){iter.remove();cash+=25}; if(a[1]>.78f && abs(a[0]-xcar)<.22f){speed*=.3f;a[1]=1.2f} }
 }
 private fun rect(c:Canvas,l:Float,t:Float,r:Float,b:Float,col:Int,rad:Float=0f) { p.color=col;p.style=Paint.Style.FILL; if(rad>0)c.drawRoundRect(l,t,r,b,rad,rad,p) else c.drawRect(l,t,r,b,p) }
 private fun label(c:Canvas,s:String,x:Float,y:Float,size:Float,col:Int=Color.WHITE) { p.color=col;p.textSize=size;p.typeface=Typeface.DEFAULT_BOLD;p.style=Paint.Style.FILL;c.drawText(s,x,y,p) }
 private fun btn(c:Canvas,l:Float,t:Float,r:Float,b:Float,s:String) { rect(c,l,t,r,b,0xCC171C2B.toInt(),12f);p.color=Color.rgb(170,255,100);p.style=Paint.Style.STROKE;p.strokeWidth=2f;c.drawRoundRect(l,t,r,b,12f,12f,p);p.style=Paint.Style.FILL;label(c,s,(l+r)/2-s.length*7f,(t+b)/2+7,20f) }
 private fun car(c:Canvas,cx:Float,cy:Float,scale:Float,col:Int) {
  val a=105f*scale;val b=54f*scale
  p.color=0x8800AFFF.toInt();c.drawOval(cx-a*.8f,cy+b*.2f,cx+a*.8f,cy+b*.7f,p)
  rect(c,cx-a*.55f,cy-b*.4f,cx+a*.55f,cy+b*.4f,col,b*.18f)
  val roof=Path();roof.moveTo(cx-a*.34f,cy-b*.35f);roof.lineTo(cx-a*.22f,cy-b*.72f);roof.lineTo(cx+a*.22f,cy-b*.72f);roof.lineTo(cx+a*.34f,cy-b*.35f);roof.close()
  p.color=Color.rgb(12,22,34);c.drawPath(roof,p)
  rect(c,cx-a*.47f,cy-b*.12f,cx-a*.3f,cy+b*.02f,Color.WHITE)
  rect(c,cx+a*.3f,cy-b*.12f,cx+a*.47f,cy+b*.02f,Color.WHITE)
  rect(c,cx-a*.62f,cy+b*.25f,cx+a*.62f,cy+b*.4f,Color.BLACK)
 }
 private fun scene(c:Canvas,w:Float,h:Float) {
  p.shader=LinearGradient(0f,0f,0f,h,intArrayOf(0xFF080D20.toInt(),0xFF302044.toInt(),0xFF090B12.toInt()),null,Shader.TileMode.CLAMP);c.drawRect(0f,0f,w,h,p);p.shader=null
  for(i in 0..13){val xx=i*w/13;val bh=h*(.15f+(i%4)*.035f);rect(c,xx,h*.48f-bh,xx+w*.05f,h*.49f,0xFF111A2B.toInt());for(j in 0..2)rect(c,xx+5+j*10,h*.48f-bh+10,xx+10+j*10,h*.48f-bh+15,0xFFB45CE8.toInt())}
  val road=Path();road.moveTo(w*.43f,h*.38f);road.lineTo(w*.57f,h*.38f);road.lineTo(w*.96f,h);road.lineTo(w*.04f,h);road.close();p.color=0xFF292B36.toInt();c.drawPath(road,p)
  for(i in 0..9){val z=((i/10f+offset/1000)%1f);val yy=h*.38f+(h*.62f)*z;rect(c,w*.498f,yy,w*.502f,yy+5+z*12,Color.LTGRAY)}
  traffic.forEach{car(c,w*(.5f+it[0]*.28f),h*(.4f+it[1]*.55f),.55f,Color.RED)}
  if(mode==1)car(c,w*(.5f+xcar*.32f),h*.8f,1f,colors[colorIndex])
  if(mode==0){label(c,"VOLKSWAGEN PASSAT B4",w*.08f,h*.25f,h*.085f,0xFFB8FF79.toInt());label(c,"UNDERGROUND 2",w*.08f,h*.36f,h*.05f);btn(c,w*.08f,h*.48f,w*.4f,h*.62f,"ИГРАТЬ");btn(c,w*.08f,h*.66f,w*.4f,h*.8f,"ГАРАЖ")}
  if(mode==2){rect(c,w*.04f,h*.08f,w*.96f,h*.92f,0xE8000000.toInt(),20f);label(c,"ГАРАЖ PASSAT B4",w*.08f,h*.25f,h*.08f,0xFFB8FF79.toInt());label(c,"Цвет кузова: касание для смены",w*.08f,h*.48f,h*.04f);label(c,"Диски • Спойлер • Обвес • Винил",w*.08f,h*.6f,h*.035f);label(c,"Баланс: \\$cash",w*.08f,h*.72f,h*.035f);btn(c,w*.68f,h*.78f,w*.92f,h*.9f,"НА ГОНКУ")}
  if(mode==1){btn(c,15f,15f,135f,65f,"МЕНЮ");label(c,"${(speed*230).toInt()} KM/H",w*.76f,45f,24f);label(c,"НИТРО",w*.76f,75f,16f);rect(c,w*.76f,85f,w*.95f,98f,Color.DKGRAY,7f);rect(c,w*.76f,85f,w*(.76f+.19f*nitro/100),98f,Color.CYAN,7f);label(c,"СЧЁТ $score    \\$cash",w*.36f,42f,20f);btn(c,15f,h*.78f,w*.15f,h*.96f,"◀");btn(c,w*.17f,h*.78f,w*.31f,h*.96f,"▶");btn(c,w*.68f,h*.78f,w*.82f,h*.96f,"ТОРМОЗ");btn(c,w*.84f,h*.78f,w*.98f,h*.96f,"ГАЗ");btn(c,w*.84f,110f,w*.98f,165f,"N₂O")}
 }
 override fun onTouchEvent(e:MotionEvent):Boolean {
  val x=e.x;val y=e.y;val w=width.toFloat();val h=height.toFloat()
  if(e.action==MotionEvent.ACTION_DOWN || e.action==MotionEvent.ACTION_MOVE){
   if(mode==1){steer=if(x<w*.16f)-1f else if(x<w*.33f)1f else 0f;brake=x>w*.68f&&x<w*.83f&&y>h*.7f;gas=x>w*.84f&&y>h*.7f;boost=x>w*.84f&&y in 110f..165f}
  }
  if(e.action==MotionEvent.ACTION_UP){
   steer=0f;gas=false;brake=false;boost=false
   if(mode==0){if(y>h*.45f&&y<h*.64f){mode=1;traffic.clear();score=0}else if(y>h*.64f&&y<h*.84f)mode=2}
   else if(mode==2){if(x>w*.65f&&y>h*.75f)mode=1 else if(y>h*.35f&&y<h*.65f){colorIndex=(colorIndex+1)%colors.size;if(cash>=250)cash-=250}}
   else if(mode==1&&x<140&&y<80)mode=0
  }
  return true
 }
}
