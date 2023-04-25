package me.av306.xenon.util.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.mixin.ClientPlayerEntityAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil
{
	public static Vec3d getEyesPos() // TODO: Rename to "getEyePos"
	{
		ClientPlayerEntity player = Xenon.INSTANCE.client.player;

		return new Vec3d(
				player.getX(),
				player.getY() + player.getEyeHeight( player.getPose() ),
				player.getZ()
		);
	}

	public static Vec3d getClientLookVec()
	{
		ClientPlayerEntity player = Xenon.INSTANCE.client.player;
		float f = 0.017453292F;
		float pi = (float) Math.PI;

		float f1 = MathHelper.cos( -player.getYaw() * f - pi );
		float f2 = MathHelper.sin( -player.getYaw() * f - pi );
		float f3 = -MathHelper.cos( -player.getPitch() * f );
		float f4 = MathHelper.sin( -player.getPitch() * f );

		return new Vec3d( f2 * f3, f4, f1 * f3 );
	}

	/**
	 * Get a Rotation object representing the amount
	 * the player needs to rotate to match a given vector.
	 *
	 * @param vec: The target vector
	 * @return The rotations needed
	 */
	public static Rotation getNeededPlayerRotations( Vec3d vec )
	{
		Vec3d eyesPos = getEyesPos();

		double diffX = vec.x - eyesPos.x;
		double diffY = vec.y - eyesPos.y;
		double diffZ = vec.z - eyesPos.z;

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) Math.toDegrees( Math.atan2( diffZ, diffX ) ) - 90F;
		float pitch = (float) -Math.toDegrees( Math.atan2( diffY, diffXZ ) );

		return new Rotation( yaw, pitch );
	}

	public static double getAngleToPlayerLookVec( Vec3d vec )
	{
		Rotation needed = getNeededPlayerRotations( vec );

		ClientPlayerEntity player = Xenon.INSTANCE.client.player;
		float currentYaw = MathHelper.wrapDegrees( player.getYaw() );
		float currentPitch = MathHelper.wrapDegrees( player.getPitch() );

		float diffYaw = currentYaw - needed.yaw;
		float diffPitch = currentPitch - needed.pitch;

		return Math.sqrt( diffYaw * diffYaw + diffPitch * diffPitch );
	}

	public static double getAngleToLastReportedLookVec(Vec3d vec)
	{
		Rotation needed = getNeededPlayerRotations( vec );

		ClientPlayerEntityAccessor player = (ClientPlayerEntityAccessor) Xenon.INSTANCE.client.player;
		float lastReportedYaw = MathHelper.wrapDegrees( player.getLastYaw() );
		float lastReportedPitch = MathHelper.wrapDegrees( player.getLastPitch() );

		float diffYaw = lastReportedYaw - needed.yaw;
		float diffPitch = lastReportedPitch - needed.pitch;

		return Math.sqrt( diffYaw * diffYaw + diffPitch * diffPitch );
	}

	public static float getHorizontalAngleToLookVec( Vec3d vec )
	{
		Rotation needed = getNeededPlayerRotations( vec );
		return MathHelper.wrapDegrees( Xenon.INSTANCE.client.player.getYaw() ) - needed.yaw;
	}

	public record Rotation( float yaw, float pitch )
	{
		public Rotation( float yaw, float pitch )
		{
			this.yaw = MathHelper.wrapDegrees( yaw );
			this.pitch = MathHelper.wrapDegrees( pitch );
		}
	}
}