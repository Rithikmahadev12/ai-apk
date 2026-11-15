package com.jarvis.assist.aosui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import com.jarvis.assist.aosui.core.MemPolicy;
import com.jarvis.assist.aosui.core.Memory;
import com.jarvis.assist.aosui.core.SizedLru;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /tmp/augmaos/classes.dex */
public final class IconCache {
    static final long DISK_MAX = 12582912;
    static IconCache I;
    final Context c;
    final File dir;
    final SizedLru<String, Bitmap> mem;
    final HashSet<String> pending = new HashSet<>();
    final Paint mask = new Paint(1);
    final Memory.Trim trim = new Memory.Trim() { // from class: com.jarvis.assist.aosui.home.IconCache$$ExternalSyntheticLambda2
        @Override // com.jarvis.assist.aosui.core.Memory.Trim
        public final void trim(int i, int i2) {
            IconCache.this.trim(i, i2);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized IconCache get(Context context) {
        IconCache iconCache;
        synchronized (IconCache.class) {
            if (I == null) {
                I = new IconCache(context.getApplicationContext());
            }
            iconCache = I;
        }
        return iconCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void trim(int i, int i2) {
        if (MemPolicy.has(i2, 64)) {
            this.mem.clear();
        } else if (MemPolicy.has(i2, 4)) {
            this.mem.trim(this.mem.size() / 2);
        }
    }

    IconCache(Context context) {
        this.c = context;
        Memory.init(context);
        this.mem = new SizedLru<>(MemPolicy.iconBudget(Memory.memClass()), new SizedLru.Sizer() { // from class: com.jarvis.assist.aosui.home.IconCache$$ExternalSyntheticLambda3
            @Override // com.jarvis.assist.aosui.core.SizedLru.Sizer
            public final int size(Object obj) {
                int allocationByteCount;
                allocationByteCount = ((Bitmap) obj).getAllocationByteCount();
                return allocationByteCount;
            }
        });
        this.dir = new File(context.getCacheDir(), "icons");
        this.dir.mkdirs();
        this.mask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        Memory.on(this.trim);
        Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.home.IconCache$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                IconCache.this.prune();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prune() {
        File[] listFiles = this.dir.listFiles();
        if (listFiles == null) {
            return;
        }
        long j = 0;
        for (File file : listFiles) {
            j += file.length();
        }
        if (j <= DISK_MAX) {
            return;
        }
        Arrays.sort(listFiles, new Comparator() { // from class: com.jarvis.assist.aosui.home.IconCache$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Long.compare(((File) obj).lastModified(), ((File) obj2).lastModified());
                return compare;
            }
        });
        for (File file2 : listFiles) {
            if (j <= 9437184) {
                return;
            }
            j -= file2.length();
            file2.delete();
        }
    }

    static String key(String str, String str2, int i) {
        return str + "/" + str2 + "@" + i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap get(final String str, final String str2, final int i, final Runnable runnable) {
        final String key = key(str, str2, i);
        Bitmap bitmap = this.mem.get(key);
        if (bitmap != null) {
            return bitmap;
        }
        synchronized (this.pending) {
            if (!this.pending.add(key)) {
                return null;
            }
            Memory.io(new Runnable() { // from class: com.jarvis.assist.aosui.home.IconCache$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    IconCache.this.m274lambda$get$2$comaugmaosohomeIconCache(str, str2, i, key, runnable);
                }
            });
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$get$2$com-augma-oso-home-IconCache, reason: not valid java name */
    public /* synthetic */ void m274lambda$get$2$comaugmaosohomeIconCache(String str, String str2, int i, String str3, Runnable runnable) {
        Bitmap load = load(str, str2, i);
        synchronized (this.pending) {
            this.pending.remove(str3);
        }
        if (load != null) {
            this.mem.put(str3, load);
            NativeHome.MAIN.post(runnable);
        }
    }

    Bitmap load(String str, String str2, int i) {
        Drawable applicationIcon;
        Bitmap decodeFile;
        File file = new File(this.dir, (str + "__" + str2).replaceAll("[^A-Za-z0-9._-]", "_") + "_" + i + ".png");
        try {
            long j = this.c.getPackageManager().getPackageInfo(str, 0).lastUpdateTime;
            if (file.exists() && file.lastModified() >= j && (decodeFile = BitmapFactory.decodeFile(file.getPath())) != null && decodeFile.getWidth() == i) {
                return decodeFile;
            }
            try {
                applicationIcon = this.c.getPackageManager().getActivityIcon(new ComponentName(str, str2));
            } catch (Exception e) {
                try {
                    applicationIcon = this.c.getPackageManager().getApplicationIcon(str);
                } catch (Exception e2) {
                    return null;
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            applicationIcon.setBounds(0, 0, i, i);
            applicationIcon.draw(canvas);
            Bitmap createBitmap2 = Bitmap.createBitmap(i, i, Bitmap.Config.ALPHA_8);
            float f = i / 2.0f;
            new Canvas(createBitmap2).drawCircle(f, f, f, new Paint(1));
            canvas.drawBitmap(createBitmap2, 0.0f, 0.0f, this.mask);
            createBitmap2.recycle();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();
                } finally {
                }
            } catch (Exception e3) {
            }
            return createBitmap;
        } catch (PackageManager.NameNotFoundException e4) {
            return null;
        }
    }

    void forget(final String str) {
        this.mem.removeIf(new Predicate() { // from class: com.jarvis.assist.aosui.home.IconCache$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean startsWith;
                startsWith = ((String) obj).startsWith(str + "/");
                return startsWith;
            }
        });
        File[] listFiles = this.dir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().startsWith(str.replaceAll("[^A-Za-z0-9._-]", "_") + "__")) {
                    file.delete();
                }
            }
        }
    }
}
