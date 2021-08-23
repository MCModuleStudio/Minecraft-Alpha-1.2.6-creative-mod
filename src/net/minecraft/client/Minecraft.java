// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.client;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockGrass;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerCreative;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumOS2;
import net.minecraft.src.EnumOSMappingHelper;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiInventoryCreative;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.MinecraftImpl;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.Teleporter;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TexureWaterFlowFX;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.ThreadSleepForever;
import net.minecraft.src.Timer;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldProviderHell;
import net.minecraft.src.WorldRenderer;
import java.awt.*;
import java.io.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

// Referenced classes of package net.minecraft.client:
//            MinecraftApplet

public abstract class Minecraft
    implements Runnable
{

    public Minecraft(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag)
    {
        mainFrame = false;
        timer = new Timer(20F);
        field_6320_i = null;
        field_6317_l = true;
        field_6316_m = false;
        currentScreen = null;
        loadingScreen = new LoadingScreenRenderer(this);
        field_9243_r = new EntityRenderer(this);
        ticksRan = 0;
        field_6282_S = 0;
        field_6310_s = null;
        field_6309_t = 0;
        field_6307_v = false;
        field_9242_w = new ModelBiped(0.0F);
        objectMouseOver = null;
        sndManager = new SoundManager();
        field_9232_X = new TextureWaterFX();
        field_9231_Y = new TextureLavaFX();
        running = true;
        field_6292_I = "";
        field_6291_J = false;
        field_6290_K = -1L;
        field_6289_L = false;
        field_6302_aa = 0;
        field_6288_M = false;
        field_6287_N = System.currentTimeMillis();
        field_6300_ab = 0;
        field_9236_T = i;
        field_9235_U = j;
        mainFrame = flag;
        mcApplet = minecraftapplet;
        new ThreadSleepForever(this, "Timer hack thread");
        mcCanvas = canvas;
        displayWidth = i;
        displayHeight = j;
        mainFrame = flag;
    }

    public abstract void func_4007_a(UnexpectedThrowable unexpectedthrowable);

    public void func_6258_a(String s, int i)
    {
        field_9234_V = s;
        field_9233_W = i;
    }

    public void startGame() throws LWJGLException
    {
        if(mcCanvas != null)
        {
            Graphics g = mcCanvas.getGraphics();
            if(g != null)
            {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, displayWidth, displayHeight);
                g.dispose();
            }
            Display.setParent(mcCanvas);
        } else
        if(mainFrame)
        {
            Display.setFullscreen(true);
            displayWidth = Display.getDisplayMode().getWidth();
            displayHeight = Display.getDisplayMode().getHeight();
            if(displayWidth <= 0)
            {
                displayWidth = 1;
            }
            if(displayHeight <= 0)
            {
                displayHeight = 1;
            }
        } else
        {
            Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(displayWidth, displayHeight));
        }
        Display.setTitle("Minecraft Minecraft Alpha v1.2.6");
        try
        {
            Display.create();
        }
        catch(LWJGLException lwjglexception)
        {
            lwjglexception.printStackTrace();
            try
            {
                Thread.sleep(1000L);
            }
            catch(InterruptedException interruptedexception) { }
            Display.create();
        }
        RenderManager.instance.field_4236_f = new ItemRenderer(this);
        field_6297_D = getMinecraftDir();
        gameSettings = new GameSettings(this, field_6297_D);
        texturePackList = new TexturePackList(this, field_6297_D);
        renderEngine = new RenderEngine(texturePackList, gameSettings);
        fontRenderer = new FontRenderer(gameSettings, "/font/default.png", renderEngine);
        loadScreen();
        Keyboard.create();
        Mouse.create();
        mouseHelper = new MouseHelper(mcCanvas);
        try
        {
            Controllers.create();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        checkGLError("Pre startup");
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glCullFace(1029);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        checkGLError("Startup");
        glCapabilities = new OpenGlCapsChecker();
        sndManager.func_340_a(gameSettings);
        renderEngine.registerTextureFX(field_9231_Y);
        renderEngine.registerTextureFX(field_9232_X);
        renderEngine.registerTextureFX(new TexturePortalFX());
        renderEngine.registerTextureFX(new TextureCompassFX(this));
        renderEngine.registerTextureFX(new TextureWatchFX(this));
        renderEngine.registerTextureFX(new TexureWaterFlowFX());
        renderEngine.registerTextureFX(new TextureLavaFlowFX());
        renderEngine.registerTextureFX(new TextureFlamesFX(0));
        renderEngine.registerTextureFX(new TextureFlamesFX(1));
        field_6323_f = new RenderGlobal(this, renderEngine);
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        field_6321_h = new EffectRenderer(theWorld, renderEngine);
        try
        {
            downloadResourcesThread = new ThreadDownloadResources(field_6297_D, this);
            downloadResourcesThread.start();
        }
        catch(Exception exception1) { }
        checkGLError("Post startup");
        ingameGUI = new GuiIngame(this);
        if(field_9234_V != null)
        {
            displayGuiScreen(new GuiConnecting(this, field_9234_V, field_9233_W));
        } else
        {
            displayGuiScreen(new GuiMainMenu());
        }
    }

    private void loadScreen() throws LWJGLException
    {
        ScaledResolution scaledresolution = new ScaledResolution(displayWidth, displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        GL11.glClear(16640);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, i, j, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2896);
        GL11.glEnable(3553);
        GL11.glDisable(2912);
        GL11.glBindTexture(3553, renderEngine.getTexture("/title/mojang.png"));
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xffffff);
        tessellator.addVertexWithUV(0.0D, displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(displayWidth, displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        char c = '\u0100';
        char c1 = '\u0100';
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.setColorOpaque_I(0xffffff);
        func_6274_a((displayWidth / 2 - c) / 2, (displayHeight / 2 - c1) / 2, 0, 0, c, c1);
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1F);
        Display.swapBuffers();
    }

    public void func_6274_a(int i, int j, int k, int l, int i1, int j1)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(i + 0, j + j1, 0.0D, (float)(k + 0) * f, (float)(l + j1) * f1);
        tessellator.addVertexWithUV(i + i1, j + j1, 0.0D, (float)(k + i1) * f, (float)(l + j1) * f1);
        tessellator.addVertexWithUV(i + i1, j + 0, 0.0D, (float)(k + i1) * f, (float)(l + 0) * f1);
        tessellator.addVertexWithUV(i + 0, j + 0, 0.0D, (float)(k + 0) * f, (float)(l + 0) * f1);
        tessellator.draw();
    }

    public static File getMinecraftDir()
    {
        if(minecraftDir == null)
        {
            minecraftDir = getAppDir("minecraft");
        }
        return minecraftDir;
    }

    public static File getAppDir(String s)
    {
        String s1 = System.getProperty("user.home", ".");
        File file;
        switch(EnumOSMappingHelper.field_1585_a[getOs().ordinal()])
        {
        case 1: // '\001'
        case 2: // '\002'
            file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
            break;

        case 3: // '\003'
            String s2 = System.getenv("APPDATA");
            if(s2 != null)
            {
                file = new File(s2, (new StringBuilder()).append(".").append(s).append('/').toString());
            } else
            {
                file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
            }
            break;

        case 4: // '\004'
            file = new File(s1, (new StringBuilder()).append("Library/Application Support/").append(s).toString());
            break;

        default:
            file = new File(s1, (new StringBuilder()).append(s).append('/').toString());
            break;
        }
        if(!file.exists() && !file.mkdirs())
        {
            throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
        } else
        {
            return file;
        }
    }

    private static EnumOS2 getOs()
    {
        String s = System.getProperty("os.name").toLowerCase();
        if(s.contains("win"))
        {
            return EnumOS2.windows;
        }
        if(s.contains("mac"))
        {
            return EnumOS2.macos;
        }
        if(s.contains("solaris"))
        {
            return EnumOS2.solaris;
        }
        if(s.contains("sunos"))
        {
            return EnumOS2.solaris;
        }
        if(s.contains("linux"))
        {
            return EnumOS2.linux;
        }
        if(s.contains("unix"))
        {
            return EnumOS2.linux;
        } else
        {
            return EnumOS2.unknown;
        }
    }

    public void displayGuiScreen(GuiScreen guiscreen)
    {
        if(currentScreen instanceof GuiUnused)
        {
            return;
        }
        if(currentScreen != null)
        {
            currentScreen.onGuiClosed();
        }
        if(guiscreen == null && theWorld == null)
        {
            guiscreen = new GuiMainMenu();
        } else
        if(guiscreen == null && thePlayer.health <= 0)
        {
            guiscreen = new GuiGameOver();
        }
        currentScreen = guiscreen;
        if(guiscreen != null)
        {
            func_6273_f();
            ScaledResolution scaledresolution = new ScaledResolution(displayWidth, displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiscreen.setWorldAndResolution(this, i, j);
            field_6307_v = false;
        } else
        {
            func_6259_e();
        }
    }

    private void checkGLError(String s)
    {
        int i = GL11.glGetError();
        if(i != 0)
        {
            String s1 = GLU.gluErrorString(i);
            System.out.println("########## GL ERROR ##########");
            System.out.println((new StringBuilder()).append("@ ").append(s).toString());
            System.out.println((new StringBuilder()).append(i).append(": ").append(s1).toString());
            //System.exit(0);
        }
    }

    public void func_6266_c()
    {
        if(mcApplet != null)
        {
            mcApplet.func_6231_c();
        }
        try
        {
            if(downloadResourcesThread != null)
            {
                downloadResourcesThread.closeMinecraft();
            }
        }
        catch(Exception exception) { }
        try
        {
            System.out.println("Stopping!");
            func_6261_a(null);
            try
            {
                GLAllocation.deleteTexturesAndDisplayLists();
            }
            catch(Exception exception1) { }
            sndManager.closeMinecraft();
            Mouse.destroy();
            Keyboard.destroy();
        }
        finally
        {
            Display.destroy();
        }
        System.gc();
    }

    public void run()
    {
        running = true;
        try
        {
            startGame();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            func_4007_a(new UnexpectedThrowable("Failed to start game", exception));
            return;
        }
        try
        {
            long l = System.currentTimeMillis();
            int i = 0;
            while(running && (mcApplet == null || mcApplet.isActive())) 
            {
                AxisAlignedBB.clearBoundingBoxPool();
                Vec3D.initialize();
                if(mcCanvas == null && Display.isCloseRequested())
                {
                    shutdown();
                }
                if(field_6316_m && theWorld != null)
                {
                    float f = timer.renderPartialTicks;
                    timer.updateTimer();
                    timer.renderPartialTicks = f;
                } else
                {
                    timer.updateTimer();
                }
                long l1 = System.nanoTime();
                for(int j = 0; j < timer.elapsedTicks; j++)
                {
                    ticksRan++;
                    try
                    {
                        runTick();
                        continue;
                    }
                    catch(MinecraftException minecraftexception)
                    {
                        theWorld = null;
                    }
                    func_6261_a(null);
                    displayGuiScreen(new GuiConflictWarning());
                }

                long l2 = System.nanoTime() - l1;
                checkGLError("Pre render");
                sndManager.func_338_a(thePlayer, timer.renderPartialTicks);
                GL11.glEnable(3553);
                if(theWorld != null && !theWorld.multiplayerWorld)
                {
                    while(theWorld.func_6465_g()) ;
                }
                if(theWorld != null && theWorld.multiplayerWorld)
                {
                    theWorld.func_6465_g();
                }
                if(gameSettings.limitFramerate)
                {
                    Thread.sleep(5L);
                }
                if(!Keyboard.isKeyDown(65))
                {
                    Display.update();
                }
                if(!field_6307_v)
                {
                    if(field_6327_b != null)
                    {
                        field_6327_b.func_6467_a(timer.renderPartialTicks);
                    }
                    field_9243_r.func_4136_b(timer.renderPartialTicks);
                }
                if(!Display.isActive())
                {
                    if(mainFrame)
                    {
                        toggleFullscreen();
                    }
                    Thread.sleep(10L);
                }
                if(Keyboard.isKeyDown(61))
                {
                    func_6238_a(l2);
                } else
                {
                    field_6290_K = System.nanoTime();
                }
                Thread.yield();
                if(Keyboard.isKeyDown(65))
                {
                    Display.update();
                }
                func_6248_s();
                if(mcCanvas != null && !mainFrame && (mcCanvas.getWidth() != displayWidth || mcCanvas.getHeight() != displayHeight))
                {
                    displayWidth = mcCanvas.getWidth();
                    displayHeight = mcCanvas.getHeight();
                    if(displayWidth <= 0)
                    {
                        displayWidth = 1;
                    }
                    if(displayHeight <= 0)
                    {
                        displayHeight = 1;
                    }
                    resize(displayWidth, displayHeight);
                }
                checkGLError("Post render");
                i++;
                field_6316_m = !isMultiplayerWorld() && currentScreen != null && currentScreen.doesGuiPauseGame();
                while(System.currentTimeMillis() >= l + 1000L) 
                {
                    field_6292_I = (new StringBuilder()).append(i).append(" fps, ").append(WorldRenderer.field_1762_b).append(" chunk updates").toString();
                    WorldRenderer.field_1762_b = 0;
                    l += 1000L;
                    i = 0;
                }
            }
        }
        catch(MinecraftError minecrafterror) { }
        catch(Throwable throwable)
        {
            theWorld = null;
            throwable.printStackTrace();
            func_4007_a(new UnexpectedThrowable("Unexpected error", throwable));
        }
        finally { }
    }

    private void func_6248_s()
    {
        if(Keyboard.isKeyDown(60))
        {
            if(!field_6291_J)
            {
                if(Keyboard.isKeyDown(59))
                {
                    ingameGUI.addChatMessage(ScreenShotHelper.func_4148_a(minecraftDir, displayWidth, displayHeight));
                }
                field_6291_J = true;
            }
        } else
        {
            field_6291_J = false;
        }
    }

    private void func_6238_a(long l)
    {
        long l1 = 0xfe502aL;
        if(field_6290_K == -1L)
        {
            field_6290_K = System.nanoTime();
        }
        long l2 = System.nanoTime();
        field_9239_F[field_9238_G & field_9240_E.length - 1] = l;
        field_9240_E[field_9238_G++ & field_9240_E.length - 1] = l2 - field_6290_K;
        field_6290_K = l2;
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(7);
        int i = (int)(l1 / 0x30d40L);
        tessellator.setColorOpaque_I(0x20000000);
        tessellator.addVertex(0.0D, displayHeight - i, 0.0D);
        tessellator.addVertex(0.0D, displayHeight, 0.0D);
        tessellator.addVertex(field_9240_E.length, displayHeight, 0.0D);
        tessellator.addVertex(field_9240_E.length, displayHeight - i, 0.0D);
        tessellator.setColorOpaque_I(0x20200000);
        tessellator.addVertex(0.0D, displayHeight - i * 2, 0.0D);
        tessellator.addVertex(0.0D, displayHeight - i, 0.0D);
        tessellator.addVertex(field_9240_E.length, displayHeight - i, 0.0D);
        tessellator.addVertex(field_9240_E.length, displayHeight - i * 2, 0.0D);
        tessellator.draw();
        long l3 = 0L;
        for(int j = 0; j < field_9240_E.length; j++)
        {
            l3 += field_9240_E[j];
        }

        int k = (int)(l3 / 0x30d40L / (long)field_9240_E.length);
        tessellator.startDrawing(7);
        tessellator.setColorOpaque_I(0x20400000);
        tessellator.addVertex(0.0D, displayHeight - k, 0.0D);
        tessellator.addVertex(0.0D, displayHeight, 0.0D);
        tessellator.addVertex(field_9240_E.length, displayHeight, 0.0D);
        tessellator.addVertex(field_9240_E.length, displayHeight - k, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(1);
        for(int i1 = 0; i1 < field_9240_E.length; i1++)
        {
            int j1 = ((i1 - field_9238_G & field_9240_E.length - 1) * 255) / field_9240_E.length;
            int k1 = (j1 * j1) / 255;
            k1 = (k1 * k1) / 255;
            int i2 = (k1 * k1) / 255;
            i2 = (i2 * i2) / 255;
            if(field_9240_E[i1] > l1)
            {
                tessellator.setColorOpaque_I(0xff000000 + k1 * 0x10000);
            } else
            {
                tessellator.setColorOpaque_I(0xff000000 + k1 * 256);
            }
            long l4 = field_9240_E[i1] / 0x30d40L;
            long l5 = field_9239_F[i1] / 0x30d40L;
            tessellator.addVertex((float)i1 + 0.5F, (float)((long)displayHeight - l4) + 0.5F, 0.0D);
            tessellator.addVertex((float)i1 + 0.5F, (float)displayHeight + 0.5F, 0.0D);
            tessellator.setColorOpaque_I(0xff000000 + k1 * 0x10000 + k1 * 256 + k1 * 1);
            tessellator.addVertex((float)i1 + 0.5F, (float)((long)displayHeight - l4) + 0.5F, 0.0D);
            tessellator.addVertex((float)i1 + 0.5F, (float)((long)displayHeight - (l4 - l5)) + 0.5F, 0.0D);
        }

        tessellator.draw();
        GL11.glEnable(3553);
    }

    public void shutdown()
    {
        running = false;
    }

    public void func_6259_e()
    {
        if(!Display.isActive())
        {
            return;
        }
        if(field_6289_L)
        {
            return;
        } else
        {
            field_6289_L = true;
            mouseHelper.func_774_a();
            displayGuiScreen(null);
            field_6302_aa = ticksRan + 10000;
            return;
        }
    }

    public void func_6273_f()
    {
        if(!field_6289_L)
        {
            return;
        }
        if(thePlayer != null)
        {
            thePlayer.func_458_k();
        }
        field_6289_L = false;
        mouseHelper.func_773_b();
    }

    public void func_6252_g()
    {
        if(currentScreen != null)
        {
            return;
        } else
        {
            displayGuiScreen(new GuiIngameMenu());
            return;
        }
    }

    private void func_6254_a(int i, boolean flag)
    {
        if(field_6327_b.field_1064_b)
        {
            return;
        }
        if(i == 0 && field_6282_S > 0)
        {
            return;
        }
        if(flag && objectMouseOver != null && objectMouseOver.typeOfHit == 0 && i == 0)
        {
            int j = objectMouseOver.blockX;
            int k = objectMouseOver.blockY;
            int l = objectMouseOver.blockZ;
            field_6327_b.sendBlockRemoving(j, k, l, objectMouseOver.sideHit);
            field_6321_h.func_1191_a(j, k, l, objectMouseOver.sideHit);
        } else
        {
            field_6327_b.func_6468_a();
        }
    }

    private void clickMouse(int i)
    {
        if(i == 0 && field_6282_S > 0)
        {
            return;
        }
        if(i == 0)
        {
            thePlayer.func_457_w();
        }
        boolean flag = true;
        if(objectMouseOver == null)
        {
            if(i == 0 && !(field_6327_b instanceof PlayerControllerTest))
            {
                field_6282_S = 10;
            }
        } else
        if(objectMouseOver.typeOfHit == 1)
        {
            if(i == 0)
            {
                field_6327_b.func_6472_b(thePlayer, objectMouseOver.entityHit);
            }
            if(i == 1)
            {
                field_6327_b.func_6475_a(thePlayer, objectMouseOver.entityHit);
            }
        } else
        if(objectMouseOver.typeOfHit == 0)
        {
            int j = objectMouseOver.blockX;
            int k = objectMouseOver.blockY;
            int l = objectMouseOver.blockZ;
            int i1 = objectMouseOver.sideHit;
            Block block = Block.blocksList[theWorld.getBlockId(j, k, l)];
            if(i == 0)
            {
                theWorld.onBlockHit(j, k, l, objectMouseOver.sideHit);
                if(block != Block.bedrock || thePlayer.field_9371_f >= 100)
                {
                    field_6327_b.clickBlock(j, k, l, objectMouseOver.sideHit);
                }
            } else
            {
                ItemStack itemstack1 = thePlayer.inventory.getCurrentItem();
                int j1 = itemstack1 == null ? 0 : itemstack1.stackSize;
                if(field_6327_b.sendPlaceBlock(thePlayer, theWorld, itemstack1, j, k, l, i1))
                {
                    flag = false;
                    thePlayer.func_457_w();
                }
                if(itemstack1 == null)
                {
                    return;
                }
                if(itemstack1.stackSize == 0)
                {
                    thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
                } else
                if(itemstack1.stackSize != j1)
                {
                    field_9243_r.field_1395_a.func_9449_b();
                }
            }
        }
        if(flag && i == 1)
        {
            ItemStack itemstack = thePlayer.inventory.getCurrentItem();
            if(itemstack != null && field_6327_b.sendUseItem(thePlayer, theWorld, itemstack))
            {
                field_9243_r.field_1395_a.func_9450_c();
            }
        }
    }

    public void toggleFullscreen()
    {
        try
        {
            mainFrame = !mainFrame;
            System.out.println("Toggle fullscreen!");
            if(mainFrame)
            {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                displayWidth = Display.getDisplayMode().getWidth();
                displayHeight = Display.getDisplayMode().getHeight();
                if(displayWidth <= 0)
                {
                    displayWidth = 1;
                }
                if(displayHeight <= 0)
                {
                    displayHeight = 1;
                }
            } else
            {
                if(mcCanvas != null)
                {
                    displayWidth = mcCanvas.getWidth();
                    displayHeight = mcCanvas.getHeight();
                } else
                {
                    displayWidth = field_9236_T;
                    displayHeight = field_9235_U;
                }
                if(displayWidth <= 0)
                {
                    displayWidth = 1;
                }
                if(displayHeight <= 0)
                {
                    displayHeight = 1;
                }
                Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(field_9236_T, field_9235_U));
            }
            func_6273_f();
            Display.setFullscreen(mainFrame);
            Display.update();
            Thread.sleep(1000L);
            if(mainFrame)
            {
                func_6259_e();
            }
            if(currentScreen != null)
            {
                func_6273_f();
                resize(displayWidth, displayHeight);
            }
            System.out.println((new StringBuilder()).append("Size: ").append(displayWidth).append(", ").append(displayHeight).toString());
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void resize(int i, int j)
    {
        if(i <= 0)
        {
            i = 1;
        }
        if(j <= 0)
        {
            j = 1;
        }
        displayWidth = i;
        displayHeight = j;
        if(currentScreen != null)
        {
            ScaledResolution scaledresolution = new ScaledResolution(i, j);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            currentScreen.setWorldAndResolution(this, k, l);
        }
    }

    private void clickMiddleMouseButton()
    {
        if(objectMouseOver != null)
        {
            int i = theWorld.getBlockId(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
            /*if(i == Block.grass.blockID)
            {
                i = Block.dirt.blockID;
            }
            if(i == Block.stairDouble.blockID)
            {
                i = Block.stairSingle.blockID;
            }
            if(i == Block.bedrock.blockID)
            {
                i = Block.stone.blockID;
            } */
            thePlayer.inventory.setCurrentItem(i, field_6327_b instanceof PlayerControllerTest);
        }
    }

    public void runTick()
    {
        ingameGUI.func_555_a();
        field_9243_r.func_910_a(1.0F);
        if(thePlayer != null)
        {
            thePlayer.func_6420_o();
        }
        if(!field_6316_m && theWorld != null)
        {
            field_6327_b.func_6474_c();
        }
        GL11.glBindTexture(3553, renderEngine.getTexture("/terrain.png"));
        if(!field_6316_m)
        {
            renderEngine.func_1067_a();
        }
        if(currentScreen == null && thePlayer != null && thePlayer.health <= 0)
        {
            displayGuiScreen(null);
        }
        if(currentScreen != null)
        {
            field_6302_aa = ticksRan + 10000;
        }
        if(currentScreen != null)
        {
            currentScreen.handleInput();
            if(currentScreen != null)
            {
                currentScreen.updateScreen();
            }
        }
        if(currentScreen == null || currentScreen.field_948_f)
        {
            do
            {
                if(!Mouse.next())
                {
                    break;
                }
                long l = System.currentTimeMillis() - field_6287_N;
                if(l <= 200L)
                {
                    int j = Mouse.getEventDWheel();
                    if(j != 0)
                    {
                        thePlayer.inventory.changeCurrentItem(j);
                    }
                    if(currentScreen == null)
                    {
                        if(!field_6289_L && Mouse.getEventButtonState())
                        {
                            func_6259_e();
                        } else
                        {
                            if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState())
                            {
                                clickMouse(0);
                                field_6302_aa = ticksRan;
                            }
                            if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState())
                            {
                                clickMouse(1);
                                field_6302_aa = ticksRan;
                            }
                            if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState())
                            {
                                clickMiddleMouseButton();
                            }
                        }
                    } else
                    if(currentScreen != null)
                    {
                        currentScreen.handleMouseInput();
                    }
                }
            } while(true);
            if(field_6282_S > 0)
            {
                field_6282_S--;
            }
            do
            {
                if(!Keyboard.next())
                {
                    break;
                }
                thePlayer.func_460_a(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(Keyboard.getEventKeyState())
                {
                    if(Keyboard.getEventKey() == 87)
                    {
                        toggleFullscreen();
                    } else
                    {
                        if(currentScreen != null)
                        {
                            currentScreen.handleKeyboardInput();
                        } else
                        {
                            if(Keyboard.getEventKey() == 1)
                            {
                                func_6252_g();
                            }
                            if(Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61))
                            {
                                forceReload();
                            }
                            if(Keyboard.getEventKey() == 63)
                            {
                                gameSettings.thirdPersonView = !gameSettings.thirdPersonView;
                            }
                            if(Keyboard.getEventKey() == gameSettings.keyBindInventory.keyCode)
                            {
                                if(thePlayer instanceof EntityPlayerCreative) displayGuiScreen(new GuiInventoryCreative(thePlayer.inventory));
                                else displayGuiScreen(new GuiInventory(thePlayer.inventory, thePlayer.inventory.craftingInventory));
                            }
                            if(Keyboard.getEventKey() == gameSettings.keyBindDrop.keyCode)
                            {
                                thePlayer.dropPlayerItemWithRandomChoice(thePlayer.inventory.decrStackSize(thePlayer.inventory.currentItem, 1), false);
                            }
                            if(isMultiplayerWorld() && Keyboard.getEventKey() == gameSettings.keyBindChat.keyCode)
                            {
                                displayGuiScreen(new GuiChat());
                            }
                        }
                        for(int i = 0; i < 9; i++)
                        {
                            if(Keyboard.getEventKey() == 2 + i)
                            {
                                thePlayer.inventory.currentItem = i;
                            }
                        }

                        if(Keyboard.getEventKey() == gameSettings.keyBindToggleFog.keyCode)
                        {
                            gameSettings.setOptionValue(4, !Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) ? 1 : -1);
                        }
                    }
                }
            } while(true);
            if(currentScreen == null)
            {
                if(Mouse.isButtonDown(0) && (float)(ticksRan - field_6302_aa) >= timer.ticksPerSecond / 4F && field_6289_L)
                {
                    clickMouse(0);
                    field_6302_aa = ticksRan;
                }
                if(Mouse.isButtonDown(1) && (float)(ticksRan - field_6302_aa) >= timer.ticksPerSecond / 4F && field_6289_L)
                {
                    clickMouse(1);
                    field_6302_aa = ticksRan;
                }
            }
            func_6254_a(0, currentScreen == null && Mouse.isButtonDown(0) && field_6289_L);
        }
        if(theWorld != null)
        {
            if(thePlayer != null)
            {
                field_6300_ab++;
                if(field_6300_ab == 30)
                {
                    field_6300_ab = 0;
                    theWorld.func_705_f(thePlayer);
                }
            }
            theWorld.difficultySetting = gameSettings.difficulty;
            if(theWorld.multiplayerWorld)
            {
                theWorld.difficultySetting = 3;
            }
            if(!field_6316_m)
            {
                field_9243_r.func_911_a();
            }
            if(!field_6316_m)
            {
                field_6323_f.func_945_d();
            }
            if(!field_6316_m)
            {
                theWorld.func_633_c();
            }
            if(!field_6316_m || isMultiplayerWorld())
            {
                theWorld.tick();
            }
            if(!field_6316_m && theWorld != null)
            {
                theWorld.randomDisplayUpdates(MathHelper.floor_double(thePlayer.posX), MathHelper.floor_double(thePlayer.posY), MathHelper.floor_double(thePlayer.posZ));
            }
            if(!field_6316_m)
            {
                field_6321_h.func_1193_a();
            }
        }
        field_6287_N = System.currentTimeMillis();
    }

    private void forceReload()
    {
        System.out.println("FORCING RELOAD!");
        sndManager = new SoundManager();
        sndManager.func_340_a(gameSettings);
        downloadResourcesThread.reloadResources();
    }

    public boolean isMultiplayerWorld()
    {
        return theWorld != null && theWorld.multiplayerWorld;
    }

    public void func_6247_b(String s)
    {
        func_6261_a(null);
        System.gc();
        World world = new World(new File(getMinecraftDir(), "saves"), s);
        if(world.field_1033_r)
        {
            func_6263_a(world, "Generating level");
        } else
        {
            func_6263_a(world, "Loading level");
        }
    }

    public void func_6237_k()
    {
        if(thePlayer.dimension == -1)
        {
            thePlayer.dimension = 0;
        } else
        {
            thePlayer.dimension = -1;
        }
        theWorld.setEntityDead(thePlayer);
        thePlayer.isDead = false;
        double d = thePlayer.posX;
        double d1 = thePlayer.posZ;
        double d2 = 8D;
        if(thePlayer.dimension == -1)
        {
            d /= d2;
            d1 /= d2;
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
            theWorld.func_4084_a(thePlayer, false);
            World world = new World(theWorld, new WorldProviderHell());
            func_6256_a(world, "Entering the Nether", thePlayer);
        } else
        {
            d *= d2;
            d1 *= d2;
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
            theWorld.func_4084_a(thePlayer, false);
            World world1 = new World(theWorld, new WorldProvider());
            func_6256_a(world1, "Leaving the Nether", thePlayer);
        }
        thePlayer.worldObj = theWorld;
        thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
        theWorld.func_4084_a(thePlayer, false);
        (new Teleporter()).func_4107_a(theWorld, thePlayer);
    }

    public void func_6261_a(World world)
    {
        func_6263_a(world, "");
    }

    public void func_6263_a(World world, String s)
    {
        func_6256_a(world, s, null);
    }

    public void func_6256_a(World world, String s, EntityPlayer entityplayer)
    {
        loadingScreen.func_596_a(s);
        loadingScreen.displayLoadingString("");
        sndManager.func_331_a(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if(theWorld != null)
        {
            theWorld.func_651_a(loadingScreen);
        }
        theWorld = world;
        System.out.println((new StringBuilder()).append("Player is ").append(thePlayer).toString());
        if(world != null)
        {
            field_6327_b.func_717_a(world);
            if(!isMultiplayerWorld())
            {
                if(entityplayer == null)
                {
                    thePlayer = (EntityPlayerSP)world.func_4085_a(EntityPlayerSP.class);
                }
            } else
            if(thePlayer != null)
            {
                thePlayer.preparePlayerToSpawn();
                if(world != null)
                {
                    world.entityJoinedWorld(thePlayer);
                }
            }
            if(!world.multiplayerWorld)
            {
                func_6255_d(s);
            }
            System.out.println((new StringBuilder()).append("Player is now ").append(thePlayer).toString());
            if(thePlayer == null)
            {
                thePlayer = (EntityPlayerSP)field_6327_b.func_4087_b(world);
                thePlayer.preparePlayerToSpawn();
                field_6327_b.flipPlayer(thePlayer);
            }
            thePlayer.field_787_a = new MovementInputFromOptions(gameSettings);
            if(field_6323_f != null)
            {
                field_6323_f.func_946_a(world);
            }
            if(field_6321_h != null)
            {
                field_6321_h.func_1188_a(world);
            }
            field_6327_b.func_6473_b(thePlayer);
            if(entityplayer != null)
            {
                world.func_6464_c();
            }
            world.func_608_a(thePlayer);
            if(world.field_1033_r)
            {
                world.func_651_a(loadingScreen);
            }
        } else
        {
            thePlayer = null;
        }
        System.gc();
        field_6287_N = 0L;
    }

    private void func_6255_d(String s)
    {
        loadingScreen.func_596_a(s);
        loadingScreen.displayLoadingString("Building terrain");
        char c = '\200';
        int i = 0;
        int j = (c * 2) / 16 + 1;
        j *= j;
        for(int k = -c; k <= c; k += 16)
        {
            int l = theWorld.spawnX;
            int i1 = theWorld.spawnZ;
            if(thePlayer != null)
            {
                l = (int)thePlayer.posX;
                i1 = (int)thePlayer.posZ;
            }
            for(int j1 = -c; j1 <= c; j1 += 16)
            {
                loadingScreen.setLoadingProgress((i++ * 100) / j);
                theWorld.getBlockId(l + k, 64, i1 + j1);
                while(theWorld.func_6465_g()) ;
            }

        }

        loadingScreen.displayLoadingString("Simulating world for a bit");
        j = 2000;
        theWorld.func_656_j();
    }

    public void installResource(String s, File file)
    {
        int i = s.indexOf("/");
        String s1 = s.substring(0, i);
        s = s.substring(i + 1);
        if(s1.equalsIgnoreCase("sound"))
        {
            sndManager.func_6372_a(s, file);
        } else
        if(s1.equalsIgnoreCase("newsound"))
        {
            sndManager.func_6372_a(s, file);
        } else
        if(s1.equalsIgnoreCase("streaming"))
        {
            sndManager.addStreaming(s, file);
        } else
        if(s1.equalsIgnoreCase("music"))
        {
            sndManager.addMusic(s, file);
        } else
        if(s1.equalsIgnoreCase("newmusic"))
        {
            sndManager.addMusic(s, file);
        }
    }

    public OpenGlCapsChecker func_6251_l()
    {
        return glCapabilities;
    }

    public String func_6241_m()
    {
        return field_6323_f.func_953_b();
    }

    public String func_6262_n()
    {
        return field_6323_f.func_957_c();
    }

    public String func_6245_o()
    {
        return (new StringBuilder()).append("P: ").append(field_6321_h.func_1190_b()).append(". T: ").append(theWorld.func_687_d()).toString();
    }

    public void respawn()
    {
        if(!theWorld.worldProvider.func_6477_d())
        {
            func_6237_k();
        }
        theWorld.func_4076_b();
        theWorld.func_9424_o();
        int i = 0;
        if(thePlayer != null)
        {
            i = thePlayer.field_620_ab;
            theWorld.setEntityDead(thePlayer);
        }
        thePlayer = (EntityPlayerSP)field_6327_b.func_4087_b(theWorld);
        thePlayer.preparePlayerToSpawn();
        field_6327_b.flipPlayer(thePlayer);
        theWorld.func_608_a(thePlayer);
        thePlayer.field_787_a = new MovementInputFromOptions(gameSettings);
        thePlayer.field_620_ab = i;
        field_6327_b.func_6473_b(thePlayer);
        func_6255_d("Respawning");
        if(currentScreen instanceof GuiGameOver)
        {
            displayGuiScreen(null);
        }
    }

    public static void func_6269_a(String s, String s1)
    {
        func_6253_a(s, s1, null);
    }

    public static void func_6253_a(String s, String s1, String s2)
    {
        boolean flag = false;
        String s3 = s;
        Frame frame = new Frame("Minecraft");
        Canvas canvas = new Canvas();
        frame.setLayout(new BorderLayout());
        frame.add(canvas, "Center");
        canvas.setPreferredSize(new Dimension(854, 480));
        frame.pack();
        frame.setLocationRelativeTo(null);
        MinecraftImpl minecraftimpl = new MinecraftImpl(frame, canvas, null, 854, 480, flag, frame);
        Thread thread = new Thread(minecraftimpl, "Minecraft main thread");
        thread.setPriority(10);
        minecraftimpl.field_6317_l = false;
        minecraftimpl.field_6319_j = "www.minecraft.net";
        if(s3 != null && s1 != null)
        {
            minecraftimpl.field_6320_i = new Session(s3, s1);
        } else
        {
            minecraftimpl.field_6320_i = new Session((new StringBuilder()).append("Player").append(System.currentTimeMillis() % 1000L).toString(), "");
        }
        if(s2 != null)
        {
            String as[] = s2.split(":");
            minecraftimpl.func_6258_a(as[0], Integer.parseInt(as[1]));
        }
        frame.setVisible(true);
        frame.addWindowListener(new GameWindowListener(minecraftimpl, thread));
        thread.start();
    }

    public static void main(String args[])
    {
        String s = (new StringBuilder()).append("Player").append(System.currentTimeMillis() % 1000L).toString();
        if(args.length > 0)
        {
            s = args[0];
        }
        String s1 = "-";
        if(args.length > 1)
        {
            s1 = args[1];
        }
        s = (new StringBuilder()).append("Player").append(System.currentTimeMillis() % 1000L).toString();
        s = "Player524";
        func_6269_a(s, s1);
    }

    public PlayerController field_6327_b;
    private boolean mainFrame;
    public int displayWidth;
    public int displayHeight;
    private OpenGlCapsChecker glCapabilities;
    private Timer timer;
    public World theWorld;
    public RenderGlobal field_6323_f;
    public EntityPlayerSP thePlayer;
    public EffectRenderer field_6321_h;
    public Session field_6320_i;
    public String field_6319_j;
    public Canvas mcCanvas;
    public boolean field_6317_l;
    public volatile boolean field_6316_m;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer field_9243_r;
    private ThreadDownloadResources downloadResourcesThread;
    private int ticksRan;
    private int field_6282_S;
    private int field_9236_T;
    private int field_9235_U;
    public String field_6310_s;
    public int field_6309_t;
    public GuiIngame ingameGUI;
    public boolean field_6307_v;
    public ModelBiped field_9242_w;
    public MovingObjectPosition objectMouseOver;
    public GameSettings gameSettings;
    protected MinecraftApplet mcApplet;
    public SoundManager sndManager;
    public MouseHelper mouseHelper;
    public TexturePackList texturePackList;
    public File field_6297_D;
    public static long field_9240_E[] = new long[512];
    public static long field_9239_F[] = new long[512];
    public static int field_9238_G = 0;
    private String field_9234_V;
    private int field_9233_W;
    private TextureWaterFX field_9232_X;
    private TextureLavaFX field_9231_Y;
    private static File minecraftDir = null;
    public volatile boolean running;
    public String field_6292_I;
    boolean field_6291_J;
    long field_6290_K;
    public boolean field_6289_L;
    private int field_6302_aa;
    public boolean field_6288_M;
    long field_6287_N;
    private int field_6300_ab;

}
