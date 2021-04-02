package paulevs.beb.math;

import java.nio.FloatBuffer;
import java.util.Locale;

public class Matrix3F {
	private static final float[][] RESULT = new float[3][3];
	private static final Matrix3F ROTATION = new Matrix3F();
	float[][] data = new float[3][3];

	public static Matrix3F identity() {
		Matrix3F matrix = new Matrix3F();
		for (int i = 0; i < 3; i++)
			matrix.data[i][i] = 1;
		return matrix;
	}

	public static Matrix3F makeRotation(Matrix3F matrix, Vec3F axis, float angle) {
		float cosT = (float) Math.cos(angle);
		float nCosT = 1 - cosT;
		float sinT = (float) Math.sin(angle);
		float xy = axis.x * axis.y;
		float xz = axis.x * axis.z;
		float yz = axis.y * axis.z;

		// Row 1
		matrix.data[0][0] = cosT + nCosT * axis.x * axis.x;
		matrix.data[1][0] = nCosT * xy - sinT * axis.z;
		matrix.data[2][0] = nCosT * xz + sinT * axis.y;

		// Row 2
		matrix.data[0][1] = nCosT * xy + sinT * axis.z;
		matrix.data[1][1] = cosT + nCosT * axis.y * axis.y;
		matrix.data[2][1] = nCosT * yz - sinT * axis.x;

		// Row 3
		matrix.data[0][2] = nCosT * xz - sinT * axis.y;
		matrix.data[1][2] = nCosT * yz + sinT * axis.x;
		matrix.data[2][2] = cosT + nCosT * axis.z * axis.z;

		return matrix;
	}

	public static Matrix3F makeRotation(Vec3F axis, float angle) {
		Matrix3F matrix = identity();
		
		if (angle == 0) {
			return matrix;
		}
		
		return makeRotation(matrix, axis, angle);
	}

	public Matrix3F setIdentity() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				data[i][j] = i == j ? 1 : 0;
		return this;
	}

	public Matrix3F scale(float scale) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				data[x][y] *= scale;
		return this;
	}

	public Matrix3F rotate(Vec3F axis, float angle) {
		if (angle == 0) {
			return this;
		}
		makeRotation(ROTATION, axis, angle);
		return this.multiple(ROTATION);
	}

	public Vec3F multiple(Vec3F input) {
		float x = input.x;
		float y = input.y;
		float z = input.z;

		input.x = data[0][0] * x + data[1][0] * y + data[2][0] * z;
		input.y = data[0][1] * x + data[1][1] * y + data[2][1] * z;
		input.z = data[0][2] * x + data[1][2] * y + data[2][2] * z;

		return input;
	}

	public Matrix3F multiple(Matrix3F matrix) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++) {
				RESULT[x][y] = 0;
				for (int i = 0; i < 3; i++)
					RESULT[x][y] += this.data[i][y] * matrix.data[x][i];
			}
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				data[x][y] = RESULT[x][y];
		return this;
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%f, %f, %f]\n[%f, %f, %f]\n[%f, %f, %f]",
				data[0][0], data[1][0], data[2][0],
				data[0][1], data[1][1], data[2][1],
				data[0][2], data[1][2], data[2][2]);
	}

	public float getValue(int x, int y) {
		return data[x][y];
	}

	public void setValue(int x, int y, float value) {
		data[x][y] = value;
	}

	public void putToBuffer(FloatBuffer buffer) {
		for (int i = 0; i < 3; i++) {
			buffer.put(data[0][i]);
			buffer.put(data[1][i]);
			buffer.put(data[2][i]);
			buffer.put(0);
		}
		for (int i = 0; i < 3; i++)
			buffer.put(0);
		buffer.put(1);
		buffer.flip();
	}

	public Matrix3F set(Matrix3F worldTransform) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				data[x][y] = worldTransform.data[x][y];
		return this;
	}
}